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

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

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

    public List<ProductDto> getProducts(String name) {
        List<Product> products = productRepository.findByName(name);

        return products
                .stream()
                .map(ProductDto::new)
                .toList();
    }

    public ProductDto updateProduct(ProductUpdateDto productUpdateDto, User seller) {
        if(!productRepository.existsById(productUpdateDto.getId())) {
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
    public void deleteProduct(String name, User seller) {
        productRepository.deleteBySellerIdAndName(seller.getId(), name);
    }

    @Transactional
    public void deleteProducts(Integer sellerId) {
        productRepository.deleteBySellerId(sellerId);
    }

}
