package RestaurantApi.restaurantapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class defines the Order model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderDTO {

    @Id
    private String id;

    private Map<String,Integer> productsOrdered = new Hashtable<>(); //String - product name, Integer - amount of the product in the key.

    private Date date;

    @Min(60)
    private int price;


    public OrderDTO(String id, Map<String, Integer> productsOrdered) {
        this.id = id;
        this.productsOrdered = productsOrdered;
    }

    public OrderDTO(String id, Map<String, Integer> productsOrdered, Date date) {
        this.id = id;
        this.productsOrdered = productsOrdered;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productsOrdered=" + productsOrdered +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
