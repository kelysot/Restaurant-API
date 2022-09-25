package RestaurantApi.restaurantapi.utils;

import RestaurantApi.restaurantapi.models.OrderDTO;
import RestaurantApi.restaurantapi.models.ProductDTO;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contains all utils for project tests.
 */
@Slf4j
public class Utils {

    /**
     * This function creates a product for the tests that use this function.
     * @return A product.
     */
    public static ProductDTO createProductData(){
        log.info("Entered createProductData function in Utils class.");
        ProductDTO product = new ProductDTO("Roasted Salmon", "Roasted Salmon on pumpkin cream, " +
                "bonfire potato and broccomini in cream sauce, white wine, nutmeg and roasted almonds",
                "https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2021/01/Baked-Salmon-8.jpg", 108);
        log.info("Finished creating the product.");

        return product;
    }

    /**
     * This function creates products for the tests that use this function.
     * @return List of products.
     */
    public static List<ProductDTO> createProducts(){
        log.info("Entered createProducts function in Utils class.");

        //If the products changed need to change the final amount in getOrderPrice test in OrderServiceTest class,
        // and the name of the product in the createOrderWhenPriceIsUnderMinimumPrice test in OrderServiceTest class.
        ProductDTO product1 = new ProductDTO("Margherita Pizza", "Tomato sauce, Mozzarella and basil",
                "https://www.vegrecipesofindia.com/wp-content/uploads/2020/12/margherita-pizza-recipe-1.jpg", 59);
        ProductDTO product2 = new ProductDTO("Polenta", "A delicate cream of fresh corn, Parmesan, Champignon, " +
                "Portobello and King of the wood mushrooms, blanched asparagus, truffle cream and Porcini",
                "https://bakeitwithlove.com/wp-content/uploads/2022/05/Polenta-sq.jpg", 48);
        ProductDTO product3 = new ProductDTO("Roasted Salmon", "Roasted Salmon on pumpkin cream, " +
                "bonfire potato and broccomini in cream sauce, white wine, nutmeg and roasted almonds",
                "https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2021/01/Baked-Salmon-8.jpg", 108);

        List<ProductDTO> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        log.info("Finished creating products.");

        return products;
    }

    /**
     * This function creates an order for the tests that use this function.
     * @return An order.
     */
    public static OrderDTO createOrderData(){
        log.info("Entered createOrderData function in Utils class.");

        Map<String,Integer> productOrdered = new HashMap<>();
        //If the productOrdered changed need to change the final amount in getOrderPrice test in OrderServiceTest class.
        productOrdered.put("Margherita Pizza", 2);
        productOrdered.put("Polenta", 1);

        OrderDTO order = new OrderDTO("632a132b8ff1862a12373ed2", productOrdered);

        log.info("Finished creating the product.");

        return order;
    }

    /**
     * This function creates orders for the tests that use this function.
     * @return List of orders.
     */
    public static List<OrderDTO> createOrders(){
        log.info("Entered createOrders function in Utils class.");

        Map<String,Integer> productOrdered = new HashMap<>();
        productOrdered.put("Margherita Pizza", 2);
        productOrdered.put("Polenta", 1);
        OrderDTO order = new OrderDTO("632a132b8ff1862a12373ed2", productOrdered);

        Map<String,Integer> productOrdered1 = new HashMap<>();
        productOrdered1.put("Roasted Salmon", 2);
        OrderDTO order1 = new OrderDTO("632a132b8ff1862a12373ed2", productOrdered1);

        List<OrderDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);

        log.info("Finished creating orders.");

        return orders;
    }

    /**
     * This function creates orders with dates that are needed for the tests that use this function.
     * @return List of orders.
     * @throws ParseException - If the createDate function doesn't succeed create a date.
     */
    public static List<OrderDTO> createOrdersWithDates() throws ParseException {
        log.info("Entered createOrdersWithDates function in Utils class.");

        Map<String,Integer> productOrdered = new HashMap<>();
        productOrdered.put("Margherita Pizza", 2);
        productOrdered.put("Polenta", 1);
        Date date = createDate("18-09-2022");
        OrderDTO order = new OrderDTO("123", productOrdered, date);

        Map<String,Integer> productOrdered1 = new HashMap<>();
        productOrdered1.put("Roasted Salmon", 2);
        date =  new Date(System.currentTimeMillis());
        OrderDTO order1 = new OrderDTO("12dsv3", productOrdered1, date);

        List<OrderDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);
        log.info("Finished creating orders.");

        return orders;
    }

    /**
     * This function creates a Date parameter from the date string.
     * @param date_string - a string of a day in dd-MM-yyyy format.
     * @return Date parameter.
     * @throws ParseException - if the function doesn't succeed to create a date parameter.
     */
    public static Date createDate(String date_string) throws ParseException {
        log.info("Entered createDate function in Utils class.");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        log.info("Going to parse the given String to Date object.");
        return formatter.parse(date_string);
    }
}

