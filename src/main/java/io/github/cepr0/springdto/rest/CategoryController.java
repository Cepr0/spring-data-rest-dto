package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryProjection;
import io.github.cepr0.springdto.dto.CategoryDto;
import io.github.cepr0.springdto.repo.CategoryRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Cepro
 * @since 2017-07-21
 */
@RequiredArgsConstructor
@RepositoryRestController
@RequestMapping("/categories")
public class CategoryController {

    @NonNull private final CategoryRepo repo;
    @NonNull private final RepositoryEntityLinks links;
    @NonNull private final PagedResourcesAssembler<CategoryProjection> assembler;

    @GetMapping("/{id}/dto")
    public ResponseEntity<?> getDto(@PathVariable("id") Integer categoryId) {
        CategoryProjection dto = repo.getDto(categoryId);
        Resource<CategoryProjection> resource = new Resource<>(dto);
        return ResponseEntity.ok(resource);
    }
    
    @GetMapping("/dto")
    public ResponseEntity<?> getDtos() {
        List<CategoryProjection> dtos = repo.getDtos();

        Link selfLink = links.linkFor(Category.class).slash("/dto").withSelfRel();
        Resources<Resource<CategoryProjection>> resources = Resources.wrap(dtos);
        resources.add(selfLink);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/dtoPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<CategoryProjection> dtos = repo.getDtos(pageable);

        Link selfLink = links.linkFor(Category.class).slash("/dtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, selfLink);

        return ResponseEntity.ok(resources);
    }

    @Bean
    public ResourceProcessor<Resource<CategoryProjection>> categoryDtoProcessor() {
        return new ResourceProcessor<Resource<CategoryProjection>>() { // Don't convert to lambda! Won't work!
            @Override
            public Resource<CategoryProjection> process(Resource<CategoryProjection> resource) {
                CategoryProjection content = resource.getContent();
                
                CategoryDto dto = new CategoryDto(content.getCategory(), content.getQuantity());
                
                Link categoryLink = links.linkForSingleResource(content.getCategory()).withRel("category");
                Link selfLink = links.linkForSingleResource(content.getCategory()).slash("/dto").withSelfRel();
    
                return new Resource<>(dto, categoryLink, selfLink);
            }
        };
    }
}
