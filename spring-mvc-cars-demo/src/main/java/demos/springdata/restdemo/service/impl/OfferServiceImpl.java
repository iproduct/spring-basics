package demos.springdata.restdemo.service.impl;

import com.fasterxml.jackson.annotation.JsonView;
import demos.springdata.restdemo.dao.OfferRepository;
import demos.springdata.restdemo.dao.UserRepository;
import demos.springdata.restdemo.events.OfferCreationEvent;
import demos.springdata.restdemo.exception.EntityNotFoundException;
import demos.springdata.restdemo.exception.InvalidEntityException;
import demos.springdata.restdemo.model.Offer;
import demos.springdata.restdemo.model.User;
import demos.springdata.restdemo.model.Views;
import demos.springdata.restdemo.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Collection<Offer> getOffers() {
        return offerRepo.findAll();
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Offer with ID=%s not found.", id)));
    }

    @Override
    public Offer createOffer(@Valid Offer offer) {
        Long authorId;
        if(offer.getSeller() != null && offer.getSeller().getId() != null) {
            authorId = offer.getSeller().getId();
        } else {
            authorId = offer.getSellerId();
        }
        if(authorId != null) {
            User author = userRepo.findById(authorId)
                    .orElseThrow(() -> new InvalidEntityException("Seller with ID=" + authorId + " does not exist."));
            offer.setSeller(author);
        }
        if(offer.getCreated() == null) {
            offer.setCreated(new Date());
        }
        offer.setModified(offer.getCreated());

        return offerRepo.save(offer);
    }

    @Override
    public Offer updateOffer(Offer offer) {
        offer.setModified(new Date());
        Offer old = getOfferById(offer.getId());
        if(offer.getSeller() != null && offer.getSeller().getId() != old.getSeller().getId())
            throw new InvalidEntityException("Seller of article could not ne changed");
        offer.setSeller(old.getSeller());
        return offerRepo.save(offer);
    }

    @Override
    public Offer deleteOffer(Long id) {
        Offer old = offerRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Offer with ID=%s not found.", id)));
        offerRepo.deleteById(id);
        return old;
    }

    @Override
    public long getOffersCount() {
        return offerRepo.count();
    }

    // Declarative transaction
    @Transactional
    public List<Offer> createOffersBatch(List<Offer> offers) {
        List<Offer> created = offers.stream()
                .map(offer -> {
                    Offer resultOffer = createOffer(offer);
                    applicationEventPublisher.publishEvent(new OfferCreationEvent(resultOffer));
                    return resultOffer;
                }).collect(Collectors.toList());
        return created;
    }

////    Programmatic transaction
//    public List<Offer> createOffersBatch(List<Offer> articles) {
//        return transactionTemplate.execute(new TransactionCallback<List<Offer>>() {
//            // the code in this method executes in a transactional context
//            public List<Offer> doInTransaction(TransactionStatus status) {
//                List<Offer> created = articles.stream()
//                        .map(article -> {
//                            try {
//                                return addOffer(article);
//                            } catch (ConstraintViolationException ex) {
//                                log.error(">>> Constraint violation inserting articles: {} - {}", article, ex.getMessage());
//                                status.setRollbackOnly();
//                                return null;
//                            }
//                        }).collect(Collectors.toList());
//                return created;
//            }
//        });
//    }

//    // Managing transaction directly using PlatformTransactionManager
//    public List<Offer> createOffersBatch(List<Offer> articles) {
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        // explicitly setting the transaction name is something that can only be done programmatically
//        def.setName("createOffersBatchTransaction");
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        def.setTimeout(5);
//
//        // Do in transaction
//        TransactionStatus status = transactionManager.getTransaction(def);
//        List<Offer> created = articles.stream()
//            .map(article -> {
//                try {
//                    Offer resultOffer = addOffer(article);
//                    applicationEventPublisher.publishEvent(new OfferCreationEvent(resultOffer));
//                    return resultOffer;
//                } catch (ConstraintViolationException ex) {
//                    log.error(">>> Constraint violation inserting article: {} - {}", article, ex.getMessage());
//                    transactionManager.rollback(status); // ROLLBACK
//                    throw ex;
//                }
//            }).collect(Collectors.toList());
//
//        transactionManager.commit(status); // COMMIT
//        return created;
//    }

    @TransactionalEventListener
    public void handleOfferCreatedTransactionCommit(OfferCreationEvent creationEvent) {
        log.info(">>> Transaction COMMIT for article: {}", creationEvent.getOffer());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleOfferCreatedTransactionRollaback(OfferCreationEvent creationEvent) {
        log.info(">>> Transaction ROLLBACK for article: {}", creationEvent.getOffer());
    }

}
