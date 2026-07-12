package josh.ecommerce.DTO;

import josh.ecommerce.Entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Integer id;

    private String name;

    private String description;

    private Integer quantity;

    private Float price;

    private Integer sellerId;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.sellerId = product.getSeller().getId();
    }

}
