package josh.ecommerce.Controller;

import jakarta.validation.Valid;
import josh.ecommerce.DTO.CartDto;
import josh.ecommerce.DTO.UserCreateDto;
import josh.ecommerce.DTO.UserDto;
import josh.ecommerce.DTO.UserUpdateDto;
import josh.ecommerce.Entity.Role;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Service.CartService;
import josh.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @PostMapping("/register")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody UserCreateDto customer) {
        if(!customer.getRole().equals(Role.CUSTOMER)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userService.existsCustomer(customer.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UserDto addedCustomer = userService.addCustomer(customer);
        if(addedCustomer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CartDto addedCart = cartService.addCart(addedCustomer.getId());
        if(addedCart == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(List.of(addedCustomer, addedCart), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateCustomer(@Valid @RequestBody UserUpdateDto updateCustomer,
                                                  @AuthenticationPrincipal User customer) {
        if(!updateCustomer.getRole().equals(Role.CUSTOMER) ||
                !Objects.equals(updateCustomer.getId(), customer.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDto updatedCustomer = userService.updateCustomer(updateCustomer);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomer(@AuthenticationPrincipal User customer) {
        cartService.deleteCart(customer.getId());
        userService.deleteCustomer(customer.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
