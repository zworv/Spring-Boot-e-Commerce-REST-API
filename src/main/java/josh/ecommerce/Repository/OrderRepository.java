package josh.ecommerce.Repository;

import josh.ecommerce.Entity.Order;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findById(Integer id);

    List<Order> findBySellerId(Integer sellerId);

    List<Order> findByCustomerId(Integer customerId);

    @Modifying
    @Query("""
UPDATE orders o
SET o.orderStatus = "CANCELED", o.orderCompleteDate = CURRENT_DATE
WHERE o.seller.id = :sellerId AND o.orderStatus = "PROCESSING"
""")
    void cancelSellerOrders(@Param("sellerId") Integer sellerId);

    void deleteById(Integer id);

}
