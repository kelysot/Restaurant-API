package RestaurantApi.restaurantapi.exceptions;

public class OrderExceptions extends Exception{

    public OrderExceptions(String message) {
        super(message);
    }

    public static String MinimumOrderAmountException(String minimumOrderAmount) {
        return "The minimum order amount is " + minimumOrderAmount + "! please add a few more items to your order.";
    }

    public static String EmptyOrderException() {
        return "The order is empty! please add a few products.";
    }
}
