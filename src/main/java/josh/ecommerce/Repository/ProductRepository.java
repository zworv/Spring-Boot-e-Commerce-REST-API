package josh.ecommerce.Repository;

import josh.ecommerce.Entity.Product;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findById(Integer id);

    List<Product> findByName(String name);

    Optional<Product> findBySellerIdAndName(Integer sellerId, String name);

    boolean existsById(Integer id);

    boolean existsBySellerIdAndName(Integer sellerId, String name);

    void deleteBySellerIdAndName(Integer sellerId, String name);

    void deleteBySellerId(Integer sellerId);

}
