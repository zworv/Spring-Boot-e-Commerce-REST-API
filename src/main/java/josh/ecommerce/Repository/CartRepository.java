package josh.ecommerce.Repository;

import josh.ecommerce.Entity.Cart;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NullMarked
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByCustomerId(Integer customerId);

    void deleteByCustomerId(Integer customerId);

}
