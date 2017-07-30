package io.github.cepr0.springdto.dto;

import io.github.cepr0.springdto.domain.Category;

/**
 * @author Cepro
 * @since 2017-07-20
 */
public interface CategoryProjection {

    Category getCategory();
    Long getQuantity();
}
