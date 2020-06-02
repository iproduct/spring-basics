package demos.springdata.restdemo.events;

import demos.springdata.restdemo.model.Offer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class OfferCreationEvent {
        private final Offer offer;
}
