package josh.ecommerce.DTO;

import josh.ecommerce.Entity.Order;
import josh.ecommerce.Entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    Integer id;

    Integer sellerId;

    Integer customerId;

    LocalDate orderStartDate;

    LocalDate orderCompleteDate;

    OrderStatus orderStatus;

    String address;

    String creditCard;

    Float price;

    List<OrderItemDto> orderItemDtoList;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.sellerId = order.getSeller().getId();
        this.customerId = order.getCustomer().getId();
        this.orderStartDate = order.getOrderStartDate();
        this.orderCompleteDate = order.getOrderCompleteDate();
        this.orderStatus = order.getOrderStatus();
        this.address = order.getAddress();
        this.creditCard = order.getCreditCard();
        this.price = order.getPrice();
    }

}
