package josh.ecommerce.Repository;

import josh.ecommerce.Entity.Product;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findById(Integer id);

    List<Product> findByName(String name);

    List<Product> findBySellerId(Integer sellerId);

    boolean existsById(Integer id);

    boolean existsBySellerIdAndId(Integer sellerId, Integer id);

    @Modifying
    @Query("""
UPDATE products p
SET p.productStatus = "DISABLED"
WHERE p.seller.id = :sellerId AND p.id = :id
""")
    void disabledBySellerIdAndId(@Param(value = "sellerId") Integer sellerId, @Param(value = "id") Integer id);

    @Modifying
    @Query("""
UPDATE products p
SET p.productStatus = "DISABLED"
WHERE p.seller.id = :sellerId
""")
    void disabledBySellerId(@Param(value = "sellerId") Integer sellerId);

}
