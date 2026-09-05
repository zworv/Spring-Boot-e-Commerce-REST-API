package josh.ecommerce.Controller;

import jakarta.validation.Valid;
import josh.ecommerce.DTO.CartDto;
import josh.ecommerce.DTO.CartItemUpdateDto;
import josh.ecommerce.Entity.ProductStatus;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Service.CartService;
import josh.ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PutMapping
    public ResponseEntity<CartDto> updateCartItem(@Valid @RequestBody CartItemUpdateDto cartItemUpdate,
                                                  @AuthenticationPrincipal User customer) {
        if(productService.existsProduct(cartItemUpdate.getProductId()) &&
                productService.getProduct(cartItemUpdate.getProductId()).getProductStatus() == ProductStatus.DISABLED &&
                cartItemUpdate.getQuantity() != 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CartDto cartDto = cartService.updateCartItem(cartItemUpdate, customer.getId());
        if(cartDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal User customer) {
        CartDto cartDto = cartService.getCart(customer.getId());
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<CartDto> clearCartItem(@AuthenticationPrincipal User customer) {
        CartDto cartDto = cartService.clearCartItem(customer.getId());
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
