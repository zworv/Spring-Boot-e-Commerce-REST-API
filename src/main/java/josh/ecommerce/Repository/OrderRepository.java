package josh.ecommerce.Repository;

import josh.ecommerce.Entity.Order;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findById(Integer id);

    List<Order> findBySellerId(Integer sellerId);

    List<Order> findByCustomerId(Integer customerId);

    void deleteById(Integer id);

}
