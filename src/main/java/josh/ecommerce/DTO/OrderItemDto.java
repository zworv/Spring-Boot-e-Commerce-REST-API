package josh.ecommerce.DTO;

import josh.ecommerce.Entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    Integer orderId;

    Integer productId;

    String name;

    Integer quantity;

    Float price;

    public OrderItemDto(OrderItem orderItem) {
        this.orderId = orderItem.getOrderItemId().getOrderId();
        this.productId = orderItem.getOrderItemId().getProductId();
        this.name = orderItem.getName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

}
