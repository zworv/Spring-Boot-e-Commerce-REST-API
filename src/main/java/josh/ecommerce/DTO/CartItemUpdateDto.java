package josh.ecommerce.DTO;

import jakarta.validation.constraints.Min;
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

    @Min(0)
    @NotNull
    private Integer quantity;

}
