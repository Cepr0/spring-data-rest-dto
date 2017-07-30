package io.github.cepr0.springdto.dto;

import io.github.cepr0.springdto.domain.Product;

/**
 * @author Cepro
 * @since 2017-07-22
 */
public interface ProductProjection {
    
    Product getProduct();
    String getName();
}
