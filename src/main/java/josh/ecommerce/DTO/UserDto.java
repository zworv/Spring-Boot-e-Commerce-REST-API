package josh.ecommerce.DTO;

import josh.ecommerce.Entity.Role;
import josh.ecommerce.Entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Integer id;

    private String username;

    private Role role;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

}
