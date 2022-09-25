package RestaurantApi.restaurantapi.controllers;

import RestaurantApi.restaurantapi.exceptions.OrderExceptions;
import RestaurantApi.restaurantapi.exceptions.ProductExceptions;
import RestaurantApi.restaurantapi.models.OrderDTO;
import RestaurantApi.restaurantapi.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * This method creates a new order.
     * @param order - the new order to create.
     * @return ResponseEntity.
     */
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO order) {
        log.trace("Entered createOrder function in OrderController class.");
        try {
            log.info("Going to create an order.");
            orderService.createOrder(order);
            return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            log.error("ConstraintViolationException happened - The server was unable to process the contained instructions.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (OrderExceptions e) {
            log.error("OrderExceptions - The user ordered under the minimum price or he didn't ordered anything.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ProductExceptions e) {
            log.error("ProductExceptions happened - The user ordered a product that doesn't exist in the database.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    /**
     * This method returns information about all the orders in the database.
     * @return ResponseEntity.
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(){
        log.trace("Entered getAllOrders function in OrderController class.");
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, orders.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * This method returns information about all the orders that were ordered from the last day.
     * @return ResponseEntity.
     */
    @GetMapping("/orders-from-last-day")
    public ResponseEntity<?> getAllOrdersFromTheLastDay(){
        log.trace("Entered getAllOrdersFromTheLastDay function in OrderController class.");
        List<OrderDTO> orders = orderService.getAllOrdersFromTheLastDay();
        return new ResponseEntity<>(orders, orders.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
