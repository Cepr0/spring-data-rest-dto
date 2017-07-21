package io.github.cepr0.springdto.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.cepr0.springdto.domain.Category;
import org.springframework.hateoas.core.Relation;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@JsonSerialize(as = InterfaceDto.class)
@Relation(value = "category", collectionRelation = "categories")
public interface InterfaceDto {

    Category getCategory();
    Long getQuantity();
}
