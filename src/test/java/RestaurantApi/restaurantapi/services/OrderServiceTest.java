package RestaurantApi.restaurantapi.services;


import RestaurantApi.restaurantapi.exceptions.OrderExceptions;
import RestaurantApi.restaurantapi.exceptions.ProductExceptions;
import RestaurantApi.restaurantapi.models.OrderDTO;
import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.repositories.OrderRepository;
import RestaurantApi.restaurantapi.repositories.ProductRepository;
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

import java.text.ParseException;
import java.util.*;

import static RestaurantApi.restaurantapi.utils.Utils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

/**
 * This class contains all the tests for OrderService class.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    private int minimumOrderAmount = 60;

    @BeforeEach
    public void setUp(){
        orderService = new OrderService(orderRepository, productRepository);
    }

    @Test
    @DisplayName("Should create a new order.")
    void createOrder() throws OrderExceptions, ProductExceptions {
        log.debug("Entered createOrder test in OrderServiceTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);
        log.info("Making the findProductByName function to return the products we saved.");
        for (ProductDTO product:products) {
            Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));
        }

        log.info("Going to create an order for the test.");
        OrderDTO order = createOrderData();

        // When:
        orderService.createOrder(order);

        // Then:
        log.info("The test checks if we run the command 'save' only once.");
        Mockito.verify(orderRepository, Mockito.times(1)).save(any(OrderDTO.class));
    }

    @Test
    @DisplayName("Should throw an exception when no products ordered.")
    void createOrderWhenNoProductsOrdered() {
        log.info("Entered createOrderWhenNoProductsOrdered test in OrderServiceTest class.");

        // Given:
        log.info("Going to create an order for the test.");
        Map<String,Integer> productOrdered = new HashMap<>();
        OrderDTO order = new OrderDTO("123", productOrdered);

        // When:
        log.info("Run createOrder function to check if we get the exception.");
        OrderExceptions orderExceptions = assertThrows(OrderExceptions.class, ()-> {
            orderService.createOrder(order);
        });

        // Then:
        log.info("The test checks if the exception contains the message that was created for it in the OrderExceptions class.");
        assertTrue(orderExceptions.getMessage().contains("The order is empty! please add a few products."));
    }

    @Test
    @DisplayName("Should throw an exception when order price is under the minimum price.")
    void createOrderWhenPriceIsUnderMinimumPrice() {
        log.info("Entered createOrderWhenPriceIsUnderMinimumPrice test in OrderServiceTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);
        log.info("Making the findProductByName function to return the products we saved.");
        for (ProductDTO product:products) {
            Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));
        }

        log.info("Going to create an order for the test.");
        Map<String,Integer> productOrdered = new HashMap<>();
        productOrdered.put("Margherita Pizza", 1);
        OrderDTO order = new OrderDTO("632a132b8ff1862a12373ed2", productOrdered);

        // When:
        log.info("Run createOrder function to check if we get the exception.");
        OrderExceptions orderExceptions = assertThrows(OrderExceptions.class, ()-> {
            orderService.createOrder(order);
        });

        // Then:
        log.info("The test checks if the exception contains the message that was created for it in the OrderExceptions class.");
        assertTrue(orderExceptions.getMessage().contains("The minimum order amount is " + minimumOrderAmount + "! please add a few more items to your order."));
    }

    @Test
    @DisplayName("Should throw an exception when the order contains products that are not in the database.")
    void createOrderWhenOrderedProductsThatAreNotInTheDatabase() {
        log.info("Entered createOrderWhenOrderedProductsThatAreNotInTheDatabase test in OrderServiceTest class.");

        // Given:
        log.info("Going to create an order for the test.");
        String productName = "Pizza";
        OrderDTO order = createOrderData();

        // When:
        log.info("Run createOrder function to check if we get the exception.");
        ProductExceptions productExceptions = assertThrows(ProductExceptions.class, ()-> {
            orderService.createOrder(order);
        });

        // Then:
        log.info("The test checks if the exception contains the message that was created for it in the OrderExceptions class.");
        assertTrue(productExceptions.getMessage().contains(""+productName+" Product not found!"));
    }

    @Test
    @DisplayName("Should get all the orders from the database.")
    void getAllOrders() {
        log.info("Entered getAllOrders test in OrderServiceTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);

        log.info("Going to create orders for the test.");
        List<OrderDTO> orders = createOrders();
        log.info("Going to save the orders for the test.");
        Mockito.when(orderRepository.saveAll(any(List.class))).thenReturn(orders);

        // When:
        log.info("Making the findAll function to return the orders we saved.");
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        List<OrderDTO> result = orderService.getAllOrders();

        // Then:
        log.info("The test checks if there are 2 orders in the database.");
        assertThat(result.size()).isEqualTo(2);
        log.info("The test checks if the id of the first order we saved is equal to the order we found.");
        assertThat(result.get(0).getId()).isEqualTo(orders.get(0).getId());
        log.info("The test checks if the id of the second order we saved is equal to the order we found.");
        assertThat(result.get(1).getId()).isEqualTo(orders.get(1).getId());
    }

    @Test
    @DisplayName("Should get all the orders from the last day from the database.")
    void getAllOrdersFromTheLastDay() throws ParseException {
        log.info("Entered getAllOrdersFromTheLastDay test in OrderServiceTest class.");

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
        log.info("Making the findAll function return the orders that were created in the last 24 hours.");
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        List<OrderDTO> result = orderService.getAllOrdersFromTheLastDay();

        // then
        log.info("The test checks if there is 1 order in the database.");
        assertThat(result.size()).isEqualTo(1);
        log.info("The test checks if the id of the order from the last day is equal to the order we found.");
        assertThat(result.get(0).getId()).isEqualTo(orders.get(0).getId());
    }

    @Test
    @DisplayName("Should calculate the price of the order.")
    void getOrderPrice() throws ProductExceptions {
        log.info("Entered getOrderPrice test in OrderServiceTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);
        log.info("Making the findProductByName function to return the products we saved.");
        for (ProductDTO product:products) {
            Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));
        }

        log.info("Going to create an order for the test.");
        OrderDTO order = createOrderData();

        // When:
        int orderPrice = orderService.getOrderPrice(order);
        int finalAmount = products.get(0).getPrice() * 2 + products.get(1).getPrice();

        // Then:
        log.info("The test checks if the orderPrice we got is equal to what it should return.");
        assertThat(orderPrice).isEqualTo(finalAmount);
    }

    @Test
    @DisplayName("Should check if the date is from last 24 hours.")
    void fromTheLastDay() throws ParseException {
        log.info("Entered fromTheLastDay test in OrderServiceTest class.");

        // Given:
        log.info("Going to create dates for the test.");
        Date datePass = createDate("18-09-2022");
        Date dateNow = new Date(System.currentTimeMillis());

        // When:
        boolean datePassCheck = orderService.fromTheLastDay(datePass);
        boolean dateNowCheck = orderService.fromTheLastDay(dateNow);

        // Then:
        log.info("The test checks if a date that isn't today returns false.");
        assertFalse(datePassCheck);
        log.info("The test checks if today's date returns true.");
        assertTrue(dateNowCheck);

    }

}
