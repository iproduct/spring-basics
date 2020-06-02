package demos.springdata.restdemo.web;

import com.fasterxml.jackson.annotation.JsonView;
import demos.springdata.restdemo.exception.InvalidEntityException;
import demos.springdata.restdemo.model.Offer;
import demos.springdata.restdemo.model.Views;
import demos.springdata.restdemo.service.OfferService;
import demos.springdata.restdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class OfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    UserService userService;

    @GetMapping
    @JsonView(Views.Offer.class)
    public Collection<Offer> getOffers() {
        return offerService.getOffers();
    }

    @GetMapping("{id}")
    @JsonView(Views.Offer.class)
    public Offer getOffers(@PathVariable long id) {
        return offerService.getOfferById(id);
    }

    @DeleteMapping("{id}")
    @JsonView(Views.Offer.class)
    public Offer deleteOffers(@PathVariable long id) {
        return offerService.deleteOffer(id);
    }

    @PostMapping
    @JsonView(Views.Offer.class)
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer, Authentication authentication) {
//        User author = userService.getUserByUsername(authentication.getName());
//        offer.setAuthor(author);
        Offer created = offerService.createOffer(offer);
        URI location = MvcUriComponentsBuilder.fromMethodName(OfferController.class, "addOffer", offer, authentication)
                .pathSegment("{id}").buildAndExpand(created.getId()).toUri() ;
        return ResponseEntity.created(location).body(created);
//        return ResponseEntity.status(303).location(location).body(created);
    }

    @PutMapping("{id}")
    @JsonView(Views.Offer.class)
    public ResponseEntity<Offer> updateOffer(@PathVariable long id, @RequestBody Offer offer) {
        if(offer.getId() != id) throw new InvalidEntityException(
                String.format("Offer ID=%s from path is different from Entity ID=%s", id, offer.getId()));
        Offer updated = offerService.updateOffer(offer);
        log.info("Offer updated: {}", updated);
        return ResponseEntity.ok(updated);
    }
}
