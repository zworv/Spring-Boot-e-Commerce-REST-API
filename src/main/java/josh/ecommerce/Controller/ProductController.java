package josh.ecommerce.Controller;

import jakarta.validation.Valid;
import josh.ecommerce.DTO.*;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductCreateDto product,
                                                 @AuthenticationPrincipal User seller) {
        ProductDto addedProduct = productService.addProduct(product, seller);
        if(addedProduct == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {
        ProductDto product = productService.getProduct(id);
        if(product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable String name) {
        List<ProductDto> products = productService.getProducts(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductUpdateDto product,
                                                    @AuthenticationPrincipal User seller) {
        ProductDto updatedProduct = productService.updateProduct(product, seller);
        if(updatedProduct == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteProduct(@PathVariable String name,
                                           @AuthenticationPrincipal User seller) {
        productService.deleteProduct(name, seller);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
