package josh.ecommerce.Repository;

import josh.ecommerce.Entity.Role;
import josh.ecommerce.Entity.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    Optional<User> findByRoleAndId(Role role, Integer id);

    Optional<User> findByRoleAndUsername(Role role, String username);

    boolean existsById(Integer id);

    boolean existsByUsername(String username);

    boolean existsByRoleAndId(Role role, Integer id);

    boolean existsByRoleAndUsername(Role role, String username);

    @Modifying
    @Query("""
Update users u
SET u.userStatus = "DISABLED"
WHERE role = :role AND id = :id
""")
    void disabledByRoleAndId(@Param("role")Role role, @Param("id") Integer id);

    void deleteById(Integer id);

    void deleteByRoleAndId(Role role, Integer id);

    void deleteByRoleAndUsername(Role role, String username);

}
