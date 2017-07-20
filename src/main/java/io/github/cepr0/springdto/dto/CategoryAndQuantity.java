package io.github.cepr0.springdto.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.cepr0.springdto.domain.Category;
import org.springframework.hateoas.core.Relation;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@JsonSerialize(as = CategoryAndQuantity.class)
@Relation(value = "category", collectionRelation = "categories")
public interface CategoryAndQuantity {

    Category getCategory();
    Long getQuantity();
}
