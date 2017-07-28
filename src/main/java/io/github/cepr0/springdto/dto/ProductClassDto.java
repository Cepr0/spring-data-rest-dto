package io.github.cepr0.springdto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.cepr0.springdto.domain.Product;
import lombok.Data;
import org.springframework.hateoas.core.Relation;

/**
 * @author Cepro
 * @since 2017-07-22
 */
@Data
@Relation(value = "product", collectionRelation = "products")
public class ProductClassDto {

    @JsonIgnore
    private final Product product;

    @JsonProperty("name")
    public String getName() {
        return product.getName();
    }
    
    public ProductClassDto(Product product) {
        this.product = product;
    }
}
