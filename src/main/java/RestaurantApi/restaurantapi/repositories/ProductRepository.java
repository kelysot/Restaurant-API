package RestaurantApi.restaurantapi.repositories;

import RestaurantApi.restaurantapi.models.ProductDTO;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<ProductDTO, String> {

    Optional<ProductDTO> findProductByName(String name);

    @NonNull
    List<ProductDTO> findAll();

}