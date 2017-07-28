package io.github.cepr0.springdto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.cepr0.springdto.domain.Product;
import lombok.Data;
import org.springframework.hateoas.core.Relation;

/**
 * @author Cepro
 * @since 2017-07-22
 */
@Data
@Relation(value = "product", collectionRelation = "products")
public class ProductDtoImpl implements ProductDto {

    @JsonIgnore
    private final Product product;
    private final String name;
}
