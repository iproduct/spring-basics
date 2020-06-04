package demos.spring.vehicles.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @NonNull
    @NotNull
    private String username;

    @NonNull
    @NotNull
    @Length(min = 4, max = 30)
    private String password;
}
