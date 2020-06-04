package demos.spring.vehicles.events;

import demos.spring.vehicles.model.Offer;
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
