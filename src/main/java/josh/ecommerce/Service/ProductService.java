package josh.ecommerce.Service;

import josh.ecommerce.DTO.ProductCreateDto;
import josh.ecommerce.DTO.ProductDto;
import josh.ecommerce.DTO.ProductUpdateDto;
import josh.ecommerce.Entity.Product;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    public boolean existsProduct(User seller, Integer id) {
        return productRepository.existsBySellerIdAndId(seller.getId(), id);
    }

    public boolean existsProduct(User seller, String name) {
        return productRepository.existsBySellerIdAndName(seller.getId(), name);
    }

    public ProductDto addProduct(ProductCreateDto productCreateDto, User seller) {
        if(productRepository.existsBySellerIdAndName(seller.getId(), productCreateDto.getName())) {
            return null;
        }

        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setQuantity(productCreateDto.getQuantity());
        product.setPrice(productCreateDto.getPrice());
        product.setSeller(seller);

        return new ProductDto(productRepository.save(product));
    }

    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(ProductDto::new)
                .toList();
    }

    public ProductDto getProduct(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        return product != null ? new ProductDto(product) : null;
    }

    public ProductDto getProduct(User seller, String name) {
        Product product = productRepository.findBySellerIdAndName(seller.getId(), name).orElse(null);
        return product != null ? new ProductDto(product) : null;
    }

    public List<ProductDto> getProducts(String name) {
        List<Product> products = productRepository.findByName(name);

        return products
                .stream()
                .map(ProductDto::new)
                .toList();
    }

    public List<ProductDto> getProducts(User seller) {
        List<Product> products = productRepository.findBySellerId(seller.getId());

        return products
                .stream()
                .map(ProductDto::new)
                .toList();
    }

    public ProductDto updateProduct(ProductUpdateDto productUpdateDto, User seller) {
        if(!productRepository.existsBySellerIdAndId(seller.getId(), productUpdateDto.getId()) ||
                (
                        this.existsProduct(seller, productUpdateDto.getName()) &&
                                !Objects.equals(this.getProduct(seller, productUpdateDto.getName()).getId(), productUpdateDto.getId())
                )
        ) {
            return null;
        }

        Product product = new Product();
        product.setId(productUpdateDto.getId());
        product.setName(productUpdateDto.getName());
        product.setDescription(productUpdateDto.getDescription());
        product.setQuantity(productUpdateDto.getQuantity());
        product.setPrice(productUpdateDto.getPrice());
        product.setSeller(seller);

        return new ProductDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Integer id, User seller) {
        cartService.deleteProductInCarts(id);
        productRepository.deleteBySellerIdAndId(seller.getId(), id);
    }

    @Transactional
    public void deleteProducts(Integer sellerId) {
        productRepository.deleteBySellerId(sellerId);
    }

}
