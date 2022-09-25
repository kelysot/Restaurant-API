package RestaurantApi.restaurantapi.services;


import RestaurantApi.restaurantapi.exceptions.ProductExceptions;
import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * This method is responsible for creating a new product.
     * @param newProduct - the new product to create.
     * @throws ProductExceptions - The user ordered a product that doesn't exist.
     */
    public void createProduct(ProductDTO newProduct) throws ProductExceptions, IOException {
        log.trace("Entered createProduct function in ProductService class.");
        Optional<ProductDTO> productOptional = productRepository.findProductByName(newProduct.getName());
        if(productOptional.isPresent()) {
            log.error("The product already exists in the database.");
            throw new ProductExceptions(ProductExceptions.ProductAlreadyExistsException(newProduct.getName()));
        }
        else {
            if(!validationOfImage(newProduct.getImage())){
                throw new ProductExceptions(ProductExceptions.ImageUrlNotWorkException());
            }
            productRepository.save(newProduct);
            log.info("newProduct was saved.");
        }

    }

    /**
     * This function returns information about all the products found in the database.
     * @return List of all ProductDTOs from the database.
     */
    public List<ProductDTO> getAllProducts() {
        log.trace("Entered getAllProducts function in ProductService class.");
        List<ProductDTO> products = productRepository.findAll();
        log.info("The variable products contain all products from the database.");
        if (products.size() > 0) {
            return products;
        }else {
            log.info("There are no products in the database.");
            return new ArrayList<>();
        }
    }

    /**
     * This function search for a product by its name.
     * @param name - the product's name.
     * @return ProductDTO
     * @throws ProductExceptions - The product doesn't exist in the database.
     */
    public ProductDTO getProductByName(String name) throws ProductExceptions{
        log.trace("Entered getProductByName function in ProductService class.");
        Optional<ProductDTO> productOptional = productRepository.findProductByName(name);
        if (productOptional.isEmpty()) {
            log.error("The product doesn't exist in the database.");
            throw new ProductExceptions(ProductExceptions.NotFoundException(name));
        }else {
            log.info("The product was found.");
            return productOptional.get();
        }
    }

    /**
     * This function check if the image URL is valid.
     * @param url - Image URL.
     * @return True if the image is valid and false otherwise.
     * @throws IOException - Problem with reading the product image URL.
     */
    public boolean validationOfImage(String url) throws IOException {
        log.trace("Entered validationOfImage function in ProductService class.");
        Image image = ImageIO.read(new URL(url));
        return image != null;
    }

}
