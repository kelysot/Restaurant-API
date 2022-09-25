package RestaurantApi.restaurantapi.services;


import RestaurantApi.restaurantapi.exceptions.ProductExceptions;
import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static RestaurantApi.restaurantapi.utils.Utils.createProductData;
import static RestaurantApi.restaurantapi.utils.Utils.createProducts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

/**
 * This class contains all the tests for ProductService class.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("Should create a new product.")
    void createProduct() throws ProductExceptions, IOException {
        log.info("Entered createProduct test in ProductServiceTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO product = createProductData();

        // When:
        productService.createProduct(product);

        // Then:
        log.info("The test checks if the command 'save' run only once.");
        Mockito.verify(productRepository, Mockito.times(1)).save(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Should throw an exception when the new product we trying to save already exists in the database.")
    void createProductWhenNameExistsInDatabase() {
        log.info("Entered createProductWhenNameExistsInDatabase test in ProductServiceTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO product = createProductData();
        log.info("Going to save the product for the test.");
        Mockito.when(productRepository.save(any(ProductDTO.class))).thenReturn(product);
        log.info("Making the findProductByName function to return the product we saved.");
        Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));

        // When:
        log.info("Run createProduct function to check if we get the exception.");
        ProductExceptions productExceptions = assertThrows(ProductExceptions.class, ()-> {
            productService.createProduct(product);
        });

        // Then:
        log.info("The test checks if the exception contains the message that was created for it in the productExceptions class.");
        assertTrue(productExceptions.getMessage().contains("Product" + product.getName() + "already exists"));
    }

    @Test
    @DisplayName("Should throw an exception when the new product we trying to save has an image URL that isn't valid.")
    void createProductWhenProductImageIsNotValid() {
        log.info("Entered createProductWhenProductImageIsNotValid test in ProductServiceTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO product = new ProductDTO("Roasted Salmon", "Roasted Salmon on pumpkin cream, " +
                "bonfire potato and broccomini in cream sauce, white wine, nutmeg and roasted almonds",
                "www.wrong-img.jpg", 108);

        // When:
        log.info("Run createProduct function to check if we get the exception.");
        IOException productExceptions = assertThrows(IOException.class, ()-> {
            productService.createProduct(product);
        });

        // Then:
        log.info("The test checks if the exception contains the message that IOException created for it.");
        assertTrue(productExceptions.getMessage().contains("no protocol"));
    }

    @Test
    @DisplayName("Should get all the products from the database.")
    void getAllProducts() {
        log.info("Entered getAllProducts test in ProductServiceTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);

        // When:
        log.info("Making the findAll function to return the products we saved.");
        Mockito.when(productRepository.findAll()).thenReturn(products);
        List<ProductDTO> result = productService.getAllProducts();

        // Then:
        log.info("The test checks if there are 3 products in the database.");
        assertThat(result.size()).isEqualTo(3);
        log.info("The test checks if the name of the first product we saved is equal to the product we found.");
        assertThat(result.get(0).getName()).isEqualTo(products.get(0).getName());
        log.info("The test checks if the name of the second product we saved is equal to the product we found.");
        assertThat(result.get(1).getName()).isEqualTo(products.get(1).getName());
    }

    @Test
    @DisplayName("Should throw an exception when the new product we trying to find doesn't exist in the database.")
    void getProductByNameWhenProductDoesNotExistInTheDatabase() {
        log.info("Entered getProductByNameWhenProductDoesNotExistInTheDatabase test in ProductServiceTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO newProduct = createProductData();
        log.info("Making the findProductByName function to return Optional.empty() because we didn't safe the newProduct.");
        Mockito.when(productRepository.findProductByName(newProduct.getName())).thenReturn(Optional.empty());

        // When:
        log.info("Run createProduct function to check if we get the exception.");
        ProductExceptions productExceptions = assertThrows(ProductExceptions.class, ()-> {
            productService.getProductByName(newProduct.getName());
        });

        // Then:
        log.info("The test checks if the exception contains the message that was created for it in the productExceptions class.");
        assertTrue(productExceptions.getMessage().contains(""+newProduct.getName() +" Product not found!"));
    }

    @Test
    @DisplayName("Should find product by name.")
    void getProductByName() throws ProductExceptions {
        log.info("Entered getProductByName test in ProductServiceTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO newProduct = createProductData();
        log.info("Making the findProductByName function to return the product we saved.");
        Mockito.when(productRepository.findProductByName(newProduct.getName())).thenReturn(Optional.of(newProduct));

        // When:
        ProductDTO product = productService.getProductByName(newProduct.getName());

        // Then:
        log.info("The test checks if the name of the product we saved is equal to the product we found.");
        Assertions.assertEquals(product.getName(), newProduct.getName());
    }

    @Test
    @DisplayName("Should validate image URL.")
    void validationOfImage() throws IOException {
        log.info("Entered validationOfImage test in ProductServiceTest class.");

        // Given:
        log.info("Going to create image URLs for the test.");
        String correctImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzN-ovSt3M2MQyBhlBlKVwX2oi6Mh8evvi3Q&usqp=CAU";

        // Then:
        log.info("The test checks if the imageURL returns true.");
        assertTrue(productService.validationOfImage(correctImageUrl));
    }

    @Test
    @DisplayName("Should throw an exception when the image URL that isn't valid.")
    void validationOfNotValidImage() {
        log.info("Entered validationOfNotValidImage test in ProductServiceTest class.");

        // Given:
        log.info("Going to create wrong image URL for the test.");
        String wrongImageUrl = "https//:www.not-good-url.com";


        // When:
        log.info("Run createProduct function to check if we get the exception.");
        IOException productExceptions = assertThrows(IOException.class, ()-> {
            productService.validationOfImage(wrongImageUrl);
        });

        // Then:
        log.info("The test checks if the exception contains the message that IOException created for it.");
        assertTrue(productExceptions.getMessage().contains("no protocol"));
    }

}
