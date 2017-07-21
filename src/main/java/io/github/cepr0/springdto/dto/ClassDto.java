package io.github.cepr0.springdto.dto;

import io.github.cepr0.springdto.domain.Category;
import lombok.Data;
import org.springframework.hateoas.core.Relation;

/**
 * @author Cepro
 *         2017-07-21
 */
@Data
@Relation(value = "category", collectionRelation = "categories")
public class ClassDto {

    private final Category category;
    private final Long quantity;
}
