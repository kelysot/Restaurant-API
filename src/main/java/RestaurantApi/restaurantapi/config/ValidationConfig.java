package RestaurantApi.restaurantapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    /**
     * The following functions are for the use of well-known annotations to ensure the validity of our fields,
     * e.g. @NotNull (required field) or @Size.
     * @return ValidatingMongoEventListener.
     */
    @Bean
    public ValidatingMongoEventListener validationMongoEventListener() {
        return new ValidatingMongoEventListener(validator()); //The validator cannot be null.
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
