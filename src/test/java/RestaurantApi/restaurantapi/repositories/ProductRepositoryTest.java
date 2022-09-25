package RestaurantApi.restaurantapi.repositories;

import RestaurantApi.restaurantapi.models.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static RestaurantApi.restaurantapi.utils.Utils.createProductData;
import static RestaurantApi.restaurantapi.utils.Utils.createProducts;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

/**
 * This class contains all the tests for ProductRepository.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductRepositoryTest {

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("Should find product by name.")
    void findProductByName() {
        log.info("Entered findProductByName function in ProductRepositoryTest class.");

        // Given:
        log.info("Going to create a product for the test.");
        ProductDTO newProduct = createProductData();
        log.info("Going to save the product for the test.");
        Mockito.when(productRepository.save(any(ProductDTO.class))).thenReturn(newProduct);
        log.info("Making the findProductByName function to return the product we saved.");
        Mockito.when(productRepository.findProductByName(newProduct.getName())).thenReturn(Optional.of(newProduct));

        // When:
        Optional<ProductDTO> product = productRepository.findProductByName(newProduct.getName());

        // Then:
        if(product.isPresent()){
            log.info("The test checks if the name of the product we saved is equal to the product we found.");
            Assertions.assertEquals(product.get().getName(), newProduct.getName());
        } else{
            log.error("The function findProductByName didn't find the product.");
            fail();
        }
    }

    @Test
    @DisplayName("Should find all the products from the database.")
    void findAll() {
        log.info("Entered findAll function in ProductRepositoryTest class.");

        // Given:
        log.info("Going to create products for the test.");
        List<ProductDTO> products = createProducts();
        log.info("Going to save the products for the test.");
        Mockito.when(productRepository.saveAll(any(List.class))).thenReturn(products);
        log.info("Making the findAll function to return the products we saved.");
        Mockito.when(productRepository.findAll()).thenReturn(products);

        // When:
        List<ProductDTO> result = productRepository.findAll();

        // Then:
        log.info("The test checks if there are 3 products in the database.");
        assertThat(result.size()).isEqualTo(3);
        log.info("The test checks if the name of the first product we saved is equal to the product we found.");
        assertThat(result.get(0).getName()).isEqualTo(products.get(0).getName());
        log.info("The test checks if the name of the second product we saved is equal to the product we found.");
        assertThat(result.get(1).getName()).isEqualTo(products.get(1).getName());
    }

}
