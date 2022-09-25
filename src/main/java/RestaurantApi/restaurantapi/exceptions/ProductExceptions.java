package RestaurantApi.restaurantapi.exceptions;

public class ProductExceptions extends Exception{

    public ProductExceptions(String message) {
        super(message);
    }

    public static String NotFoundException(String name) {
        return ""+name+" Product not found!";
    }

    public static String ImageUrlNotWorkException() {
        return "Problem with reading the product image URL";
    }

    public static String ProductAlreadyExistsException(String name) {
        return "Product" + name + "already exists";
    }
}

