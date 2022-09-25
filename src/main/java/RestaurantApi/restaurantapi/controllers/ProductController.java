package RestaurantApi.restaurantapi.controllers;

import RestaurantApi.restaurantapi.exceptions.ProductExceptions;
import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * This method is responsible for creating a new product.
     * @param newProduct - the new product to create.
     * @return ResponseEntity.
     */
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO newProduct) {
        log.trace("Entered createProduct function in ProductController class.");
        try {
            log.info("Going to create a product.");
            productService.createProduct(newProduct);
            return new ResponseEntity<ProductDTO>(newProduct, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            log.error("ConstraintViolationException happened - The server was unable to process the contained instructions.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProductExceptions e) {
            if(e.getMessage().equals("Problem with reading the product image URL")){
                log.error("ProductExceptions happened - Problem with reading the product image url.", e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                log.error("ProductExceptions happened - The product already exists in the database or problem with image url.", e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            log.error("IOException happened - Problem with reading the product image URL.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This function returns information about all the products found in the database.
     * @return ResponseEntity.
     */
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(){
        log.trace("Entered getAllProducts function in ProductController class.");
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, products.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * This function search for a product by its name.
     * @param name - the product's name.
     * @return ResponseEntity.
     */
    @GetMapping("/products/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable("name") String name){
        log.trace("Entered getProductByName function in ProductController class.");
        try {
            ProductDTO product = productService.getProductByName(name);
            log.info("Going to search for the product by its name.");
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (ProductExceptions e) {
            log.error("ProductExceptions happened - The product doesn't exist in the database.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

