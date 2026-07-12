package josh.ecommerce.DTO;

import jakarta.validation.constraints.NotNull;
import josh.ecommerce.Entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateDto {

    @NotNull
    private Integer id;

    @NotNull
    private OrderStatus orderStatus;

}
