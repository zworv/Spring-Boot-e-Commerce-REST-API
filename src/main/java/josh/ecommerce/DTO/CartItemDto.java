package josh.ecommerce.DTO;

import josh.ecommerce.Entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {

    private Integer id;

    private Integer cartId;

    private Integer productId;

    private String name;

    private Integer quantity;

    private Float price;

    public CartItemDto(CartItem cartItem) {
        this.id = cartItem.getId();
        this.cartId = cartItem.getCart().getId();
        this.productId = cartItem.getProduct().getId();
        this.name = cartItem.getName();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }

}
