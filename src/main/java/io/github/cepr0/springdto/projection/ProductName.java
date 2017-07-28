package io.github.cepr0.springdto.projection;

import io.github.cepr0.springdto.domain.Product;
import org.springframework.data.rest.core.config.Projection;

/**
 * @author Cepro
 *         2017-07-28
 */
@Projection(name = "productName", types = {Product.class})
public interface ProductName {
    String getName();
}
