package io.github.cepr0.springdto.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.core.Relation;

/**
 * @author Cepro
 * @since 2017-07-22
 */
@JsonSerialize(as = ProductInterfaceDto.class)
@Relation(value = "product", collectionRelation = "products")
public interface ProductInterfaceDto {
    
    String getName();
}
