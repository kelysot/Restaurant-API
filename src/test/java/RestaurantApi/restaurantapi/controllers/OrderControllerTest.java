package RestaurantApi.restaurantapi.controllers;


import RestaurantApi.restaurantapi.models.OrderDTO;
import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.repositories.OrderRepository;
import RestaurantApi.restaurantapi.repositories.ProductRepository;
import RestaurantApi.restaurantapi.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static RestaurantApi.restaurantapi.utils.Utils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderControllerTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @BeforeEach
    public void setUp(){
        orderService = new OrderService(orderRepository, productRepository);
        orderController = new OrderController(orderService);
    }

    @Test
    @DisplayName("Should create a new order.")
    void createOrder() {
        log.info("Entered createOrder function test in OrderControllerTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);
        log.info("Going to save products for the test.");
        for (ProductDTO product:products) {
            Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));
        }

        log.info("Create order for the test.");
        OrderDTO order = createOrderData();

        // When:
        ResponseEntity<?> responseEntity = orderController.createOrder(order);

        // Then:
        log.info("The test checks if the status code value is 200.");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        log.info("The test checks if the body of responseEntity is equal to the order that was saved.");
        assertThat(responseEntity.getBody()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should get all the orders from the database.")
    void getAllOrders() {
        log.info("Entered getAllOrders function test in OrderControllerTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);

        log.info("Going to create orders for the test.");
        List<OrderDTO> orders = createOrders();
        log.info("Going to save the orders for the test.");
        Mockito.when(orderRepository.saveAll(any(List.class))).thenReturn(orders);

        // When:
        log.info("Making the findAll function to return the orders we saved.");
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        ResponseEntity<?> responseEntity = orderController.getAllOrders();

        // Then:
        log.info("The test checks if the status code value is 200.");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        log.info("The test checks if the body of responseEntity is equal to the orders that were saved.");
        assertThat(responseEntity.getBody()).isEqualTo(orders);
    }

    @Test
    @DisplayName("Should get all the orders from the last day from the database.")
    void getAllOrdersFromTheLastDay() throws ParseException {
        log.info("Entered getAllOrdersFromTheLastDay function test in OrderControllerTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);

        log.info("Going to create orders for the test.");
        List<OrderDTO> orders = createOrdersWithDates();
        log.info("Going to save the orders for the test.");
        Mockito.when(orderRepository.saveAll(any(List.class))).thenReturn(orders);

        // When:
        log.info("Going to remove the order that didn't create in the last 24 hours.");
        orders.remove(orders.get(0));
        log.info("Making the findAll function to return the orders we saved.");
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        ResponseEntity<?> responseEntity = orderController.getAllOrdersFromTheLastDay();

        // Then:
        log.info("The test checks if the status code value is 200.");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        log.info("The test checks if the body of responseEntity is equal to the orders that were saved.");
        assertThat(responseEntity.getBody()).isEqualTo(orders);

    }

}
