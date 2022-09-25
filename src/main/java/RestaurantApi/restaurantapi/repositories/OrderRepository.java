package RestaurantApi.restaurantapi.repositories;

import RestaurantApi.restaurantapi.models.OrderDTO;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderDTO, String> {

    @NonNull
    List<OrderDTO> findAll();
}

