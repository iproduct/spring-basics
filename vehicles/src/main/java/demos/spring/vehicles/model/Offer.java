package demos.spring.vehicles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Offer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @NotNull
    private VehicleCategory category;

    @NonNull
    @NotNull
    @ManyToOne
    private Model model;

    @NonNull
    @NotNull
//    @PastOrPresent
    @Min(1900)
    private Integer year = 2020;

    @NonNull
    @NotNull
    @Positive
    private Integer mileage;

    @NonNull
    @NotNull
    private EngineType engine;

    @NonNull
    @NotNull
    private TransmissionType transmission;

    @Length(min = 2, max = 512)
    private String description;

    @NonNull
    @Positive
    private Double price;

//    @NonNull
    @Length(min = 8, max = 512)
    private String imageUrl;

    @ManyToOne(optional = true)
    @ToString.Exclude
    private User seller;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private Long modelId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private Long sellerId;


    private Date created = new Date();
    private Date modified = new Date();
}
