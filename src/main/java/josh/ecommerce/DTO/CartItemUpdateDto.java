package josh.ecommerce.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateDto {

    @NotNull
    private Integer cartId;

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;

}
