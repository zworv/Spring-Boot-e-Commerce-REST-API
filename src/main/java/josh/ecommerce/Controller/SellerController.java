package josh.ecommerce.Controller;

import jakarta.validation.Valid;
import josh.ecommerce.DTO.UserCreateDto;
import josh.ecommerce.DTO.UserDto;
import josh.ecommerce.DTO.UserUpdateDto;
import josh.ecommerce.Entity.Role;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Service.CartService;
import josh.ecommerce.Service.OrderService;
import josh.ecommerce.Service.ProductService;
import josh.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> addSeller(@Valid @RequestBody UserCreateDto seller) {
        if(!seller.getRole().equals(Role.SELLER)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userService.existsUsername(seller.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UserDto addedSeller = userService.addSeller(seller);
        if(addedSeller == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(addedSeller, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateSeller(@Valid @RequestBody UserUpdateDto updateSeller,
                                             @AuthenticationPrincipal User seller) {
        if(!updateSeller.getRole().equals(Role.SELLER) ||
                !Objects.equals(updateSeller.getId(), seller.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDto updatedSeller = userService.updateSeller(updateSeller);

        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSeller(@AuthenticationPrincipal User seller) {
        orderService.cancelSellerOrders(seller.getId());
        cartService.deleteSellerProductsInCarts(seller.getId());
        productService.disabledProducts(seller.getId());
        userService.disabledSeller(seller.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
