package RestaurantApi.restaurantapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class defines the Product model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class ProductDTO {

    @Id
    private String id;

    @Size(min=2, max=30)
    @NotNull(message="Name cannot be null")
    private String name;

    @NotNull(message="Description cannot be null")
    private String description;

    @NotNull(message="Image cannot be null")
    private String image;

    @Min(8)
    private int price;


    public ProductDTO(String name, String description, String image, int price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public ProductDTO(ProductDTO newProduct) {
        this.id = newProduct.getId();
        this.name = newProduct.getName();
        this.description = newProduct.getDescription();
        this.image = newProduct.getImage();
        this.price = newProduct.getPrice();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                '}';
    }
}
