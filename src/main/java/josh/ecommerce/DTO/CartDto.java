package josh.ecommerce.DTO;

import josh.ecommerce.Entity.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {

    private Integer id;

    private Integer customerId;

    private Float price;

    private List<CartItemDto> cartItemDtoList;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.customerId = cart.getCustomer().getId();
        this.price = cart.getPrice();
    }

}
