package RestaurantApi.restaurantapi.services;


import RestaurantApi.restaurantapi.exceptions.OrderExceptions;
import RestaurantApi.restaurantapi.exceptions.ProductExceptions;
import RestaurantApi.restaurantapi.models.OrderDTO;
import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.repositories.OrderRepository;
import RestaurantApi.restaurantapi.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    static final long DAY = 24 * 60 * 60 * 1000;
    private int minimumOrderAmount = 60;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * This method creates a new order.
     * @param newOrder - the new order to create.
     * @throws OrderExceptions - The user ordered under the minimum price.
     * @throws ProductExceptions - The user ordered a product that doesn't exist.
     */
    public void createOrder(OrderDTO newOrder) throws OrderExceptions, ProductExceptions {
        log.trace("Entered createOrder function in OrderService class.");
        if(newOrder.getProductsOrdered().isEmpty()){
            throw new OrderExceptions(OrderExceptions.EmptyOrderException());
        }
        newOrder.setPrice(getOrderPrice(newOrder));
        log.info("newOrder price was set.");
        if(newOrder.getPrice() < minimumOrderAmount) {
            log.error("The user ordered under the minimum price.");
            throw new OrderExceptions(OrderExceptions.MinimumOrderAmountException(String.valueOf(minimumOrderAmount)));
        }
        else {
            newOrder.setDate(new Date(System.currentTimeMillis()));
            log.info("newOrder date was set.");
            orderRepository.save(newOrder);
            log.info("newOrder was saved.");
        }

    }

    /**
     * This method returns information about all the orders in the database.
     * @return List of all orders from the database.
     */
    public List<OrderDTO> getAllOrders() {
        log.trace("Entered getAllOrders function in OrderService class.");
        List<OrderDTO> orders = orderRepository.findAll();
        log.info("The variable orders contain all orders from the database.");
        if (orders.size() > 0) {
            return orders;
        } else {
            log.info("There are no orders in the database.");
            return new ArrayList<>();
        }
    }

    /**
     * This method returns information about all the orders that were ordered from the last day.
     * @return List of all orders that were ordered from the last day.
     */
    public List<OrderDTO> getAllOrdersFromTheLastDay() {
        log.trace("Entered getAllOrdersFromTheLastDay function in OrderService class.");
        List<OrderDTO> orders = orderRepository.findAll();
        log.info("The variable orders contain all orders from the database.");
        if (orders.size() > 0) {
            orders.removeIf(order -> !fromTheLastDay(order.getDate()));
            log.info("The variable orders contain only the orders that were ordered from the last day.");
            return orders;
        } else {
            log.info("There are no orders in the database.");
            return new ArrayList<>();
        }
    }

    /********Additional functions*********/

    /**
     * This method calculates the price of the order.
     * @param order - the order whose price needs to be calculated.
     * @return order's price.
     * @throws ProductExceptions - The user ordered a product that doesn't exist.
     */
    public int getOrderPrice(@NotNull OrderDTO order) throws ProductExceptions {
        log.trace("Entered getOrderPrice function in OrderService class.");
        int sum = 0;

        log.info("Going to check that the desired products that the user tried to order exist in the database and calculate their sum.");
        for (Map.Entry<String, Integer> entry : order.getProductsOrdered().entrySet()) {
            Optional<ProductDTO> product = productRepository.findProductByName(entry.getKey());
            if(product.isPresent()){
                sum += product.get().getPrice() * entry.getValue(); //Calculation of the order price.
            } else {
                log.error("The user ordered a product that doesn't exist in the database.");
                throw new ProductExceptions(ProductExceptions.NotFoundException(entry.getKey()));
            }
        }
        log.info("Completion of the calculation of the price of the order.");

        return sum;
    }

    /**
     * This function checks if the date of order happened from the last day.
     * @param date - order's date of creation.
     * @return True if the order's date of creation is from the last day, and false otherwise.
     */
    public boolean fromTheLastDay(Date date) {
        log.trace("Entered fromTheLastDay function in OrderService class.");
        return date.getTime() > System.currentTimeMillis() - DAY;
    }

}
