package demos.springdata.restdemo.service;

import demos.springdata.restdemo.model.Offer;

import java.util.Collection;
import java.util.List;

public interface OfferService {
    Collection<Offer> getOffers();
    Offer getOfferById(Long id);
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
    Offer deleteOffer(Long id);
    long getOffersCount();
    List<Offer> createOffersBatch(List<Offer> offers);
}
