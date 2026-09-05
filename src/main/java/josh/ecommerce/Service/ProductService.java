package josh.ecommerce.Service;

import josh.ecommerce.DTO.ProductCreateDto;
import josh.ecommerce.DTO.ProductDto;
import josh.ecommerce.DTO.ProductUpdateDto;
import josh.ecommerce.Entity.Product;
import josh.ecommerce.Entity.ProductStatus;
import josh.ecommerce.Entity.User;
import josh.ecommerce.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public boolean existsProduct(Integer id) {
        return productRepository.existsById(id);
    }

    public ProductDto addProduct(ProductCreateDto productCreateDto, User seller) {
        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setQuantity(productCreateDto.getQuantity());
        product.setPrice(productCreateDto.getPrice());
        product.setProductStatus(ProductStatus.ENABLED);
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
        if(!productRepository.existsBySellerIdAndId(seller.getId(), productUpdateDto.getId())) {
            return null;
        }

        Product product = productRepository.findById(productUpdateDto.getId()).orElse(null);
        if(product == null) {
            return null;
        }
        product.setName(productUpdateDto.getName());
        product.setDescription(productUpdateDto.getDescription());
        product.setQuantity(productUpdateDto.getQuantity());
        product.setPrice(productUpdateDto.getPrice());

        return new ProductDto(productRepository.save(product));
    }

    @Transactional
    public void disabledProduct(Integer id, User seller) {
        productRepository.disabledBySellerIdAndId(seller.getId(), id);
    }

    @Transactional
    public void disabledProducts(Integer sellerId) {
        productRepository.disabledBySellerId(sellerId);
    }

}
