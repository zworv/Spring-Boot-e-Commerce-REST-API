package josh.ecommerce.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductUpdateDto {

    @NotNull
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(0)
    @NotNull
    private Integer quantity;

    @Min(0)
    @NotNull
    private Float price;

}
