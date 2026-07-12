package josh.ecommerce.Controller;

import jakarta.validation.Valid;
import josh.ecommerce.DTO.CartDto;
import josh.ecommerce.DTO.UserCreateDto;
import josh.ecommerce.DTO.UserDto;
import josh.ecommerce.DTO.UserUpdateDto;
import josh.ecommerce.Entity.Role;
import josh.ecommerce.Service.CartService;
import josh.ecommerce.Service.ProductService;
import josh.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<List<UserDto>>> getSellersAndCustomers() {
        List<UserDto> listOfSellers = userService.getSellers();
        List<UserDto> listOfCustomers = userService.getCustomers();
        return new ResponseEntity<>(List.of(listOfSellers, listOfCustomers), HttpStatus.OK);
    }

    @PostMapping("/sellers")
    public ResponseEntity<UserDto> addSeller(@Valid @RequestBody UserCreateDto seller) {
        if(!seller.getRole().equals(Role.SELLER)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userService.existsSeller(seller.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        UserDto addedSeller = userService.addSeller(seller);
        if(addedSeller == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(addedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<UserDto>> getSellers() {
        List<UserDto> sellers = userService.getSellers();
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @GetMapping("/sellers/{id}")
    public ResponseEntity<UserDto> getSeller(@PathVariable Integer id) {
        UserDto seller = userService.getSeller(id);
        if(seller == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PutMapping("/sellers")
    public ResponseEntity<UserDto> updateSeller(@Valid @RequestBody UserUpdateDto seller) {
        if(!seller.getRole().equals(Role.SELLER)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.existsSeller(seller.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDto updatedSeller = userService.updateSeller(seller);

        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }

    @DeleteMapping("/sellers/{id}")
    public ResponseEntity<UserDto> deleteSeller(@PathVariable Integer id) {
        if(!userService.existsSeller(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productService.deleteProducts(id);
        userService.deleteSeller(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/customers")
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

    @GetMapping("/customers")
    public ResponseEntity<List<UserDto>> getCustomers() {
        List<UserDto> customers = userService.getCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<UserDto> getCustomer(@PathVariable Integer id) {
        UserDto customer = userService.getCustomer(id);
        if(customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/customers")
    public ResponseEntity<UserDto> updateCustomer(@Valid @RequestBody UserUpdateDto customer) {
        if(!customer.getRole().equals(Role.CUSTOMER)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.existsCustomer(customer.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDto updatedCustomer = userService.updateCustomer(customer);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        if(!userService.existsCustomer(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        cartService.deleteCart(id);
        userService.deleteCustomer(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
