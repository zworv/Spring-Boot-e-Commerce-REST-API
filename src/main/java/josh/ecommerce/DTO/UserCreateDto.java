package josh.ecommerce.DTO;

import jakarta.validation.constraints.NotBlank;
import josh.ecommerce.Entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCreateDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private Role role;

}
