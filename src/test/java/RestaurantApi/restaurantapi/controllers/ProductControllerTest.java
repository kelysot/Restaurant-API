package RestaurantApi.restaurantapi.controllers;


import RestaurantApi.restaurantapi.models.ProductDTO;
import RestaurantApi.restaurantapi.repositories.ProductRepository;
import RestaurantApi.restaurantapi.services.ProductService;
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

import java.util.List;
import java.util.Optional;

import static RestaurantApi.restaurantapi.utils.Utils.createProductData;
import static RestaurantApi.restaurantapi.utils.Utils.createProducts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductControllerTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    @BeforeEach
    public void setUp(){
        productService = new ProductService(productRepository);
        productController = new ProductController(productService);
    }

    @Test
    @DisplayName("Should create a new product.")
    void createProduct() {
        log.info("Entered createProduct test in ProductControllerTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO product = createProductData();
        log.info("Going to save the product for the test.");
        Mockito.when(productRepository.save(any(ProductDTO.class))).thenReturn(product);

        // When:
        ResponseEntity<?> responseEntity = productController.createProduct(product);

        // Then:
        log.info("The test checks if the status code value is 200.");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        log.info("The test checks if the body of responseEntity is equal to the product that was saved.");
        assertThat(responseEntity.getBody()).isEqualTo(product);
    }

    @Test
    @DisplayName("Should get all the products from the database.")
    void getAllProducts() {
        log.info("Entered getAllProducts test in ProductControllerTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);

        // When:
        log.info("Making the findAll function to return the products we saved.");
        Mockito.when(productRepository.findAll()).thenReturn(products);
        ResponseEntity<?> responseEntity = productController.getAllProducts();

        // Then:
        log.info("The test checks if the status code value is 200.");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        log.info("The test checks if the body of responseEntity is equal to the products that were saved.");
        assertThat(responseEntity.getBody()).isEqualTo(products);

    }

    @Test
    @DisplayName("Should find product by name.")
    void getProductByName() {
        log.info("Entered getProductByName test in ProductControllerTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO product = createProductData();
        log.info("Going to save the product for the test.");
        Mockito.when(productRepository.save(any(ProductDTO.class))).thenReturn(product);
        log.info("Making the findProductByName function to return the product we saved.");
        Mockito.when(productRepository.findProductByName(product.getName())).thenReturn(Optional.of(product));

        // When:
        ResponseEntity<?> responseEntity = productController.getProductByName(product.getName());

        // Then:
        log.info("The test checks if the status code value is 200.");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        log.info("The test checks if the body of responseEntity is equal to the product we wanted to get.");
        assertThat(responseEntity.getBody()).isEqualTo(product);
    }

}
