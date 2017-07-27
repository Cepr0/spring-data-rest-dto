package io.github.cepr0.springdto.dto;

import io.github.cepr0.springdto.domain.Category;
import lombok.Data;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

import static io.github.cepr0.springdto.Application.links;

/**
 * @author Cepro
 *         2017-07-21
 */
@Data
@Relation(value = "category", collectionRelation = "categories")
public class CategoryClassDto extends Resource<Category> {

    private final Category category;
    private final Long quantity;
    
    public CategoryClassDto(Category category, Long quantity) {
        super(category, links().linkForSingleResource(category).withRel("category"));
        this.category = category;
        this.quantity = quantity;
    }
}
