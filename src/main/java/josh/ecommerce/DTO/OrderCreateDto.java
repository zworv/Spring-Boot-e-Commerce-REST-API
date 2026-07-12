package josh.ecommerce.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateDto {

    @NotBlank
    String address;

    @NotBlank
    String creditCard;

}
