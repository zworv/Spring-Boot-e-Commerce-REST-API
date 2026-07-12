package josh.ecommerce.Repository;

import josh.ecommerce.Entity.OrderItem;
import josh.ecommerce.Entity.OrderItemId;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@NullMarked
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    List<OrderItem> findByOrderId(Integer orderId);

    void deleteByOrderId(Integer orderId);

}
