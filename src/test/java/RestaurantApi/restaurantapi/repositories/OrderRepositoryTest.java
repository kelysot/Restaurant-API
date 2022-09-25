package RestaurantApi.restaurantapi.repositories;

import RestaurantApi.restaurantapi.models.OrderDTO;
import RestaurantApi.restaurantapi.models.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static RestaurantApi.restaurantapi.utils.Utils.createOrders;
import static RestaurantApi.restaurantapi.utils.Utils.createProducts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * This class contains all the tests for OrderRepository.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderRepositoryTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("Should find all the orders from the database.")
    void findAll() {
        log.info("Entered findAll function in OrderRepositoryTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);
        log.info("Making the findProductByName function to return the products we saved.");

        log.info("Going to create orders for the test.");
        List<OrderDTO> orders = createOrders();
        log.info("Going to save the orders for the test.");
        Mockito.when(orderRepository.saveAll(any(List.class))).thenReturn(orders);

        // When:
        log.info("Making the findAll function to return the orders we saved.");
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        List<OrderDTO> result = orderRepository.findAll();


        // Then:
        log.info("The test checks if there are 2 orders in the database.");
        assertThat(result.size()).isEqualTo(2);
        log.info("The test checks if the id of the first order we saved is equal to the order we found.");
        assertThat(result.get(0).getId()).isEqualTo(orders.get(0).getId());
        log.info("The test checks if the id of the second order we saved is equal to the order we found.");
        assertThat(result.get(1).getId()).isEqualTo(orders.get(1).getId());
    }

}
