package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryDto;
import io.github.cepr0.springdto.dto.CategoryProjection;
import io.github.cepr0.springdto.repo.CategoryRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

import static java.util.stream.Collectors.toList;

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
        
        return ResponseEntity.ok(toResource(dto));
    }
    
    @GetMapping("/dto")
    public ResponseEntity<?> getDtos() {
        List<CategoryProjection> dtos = repo.getDtos();
    
        Link listSelfLink = links.linkFor(Category.class).slash("/dto").withSelfRel();
        List<?> resources = dtos.stream().map(this::toResource).collect(toList());

        return ResponseEntity.ok(new Resources<>(resources, listSelfLink));
    }

    @GetMapping("/dtoPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<CategoryProjection> dtos = repo.getDtos(pageable);

        Link pageSelfLink = links.linkFor(Category.class).slash("/dtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, this::toResource, pageSelfLink);

        return ResponseEntity.ok(resources);
    }

    private ResourceSupport toResource(CategoryProjection projection) {
        CategoryDto dto = new CategoryDto(projection.getCategory(), projection.getQuantity());
        
        Link categoryLink = links.linkForSingleResource(projection.getCategory()).withRel("category");
        Link selfLink = links.linkForSingleResource(projection.getCategory()).slash("/dto").withSelfRel();
        
        return new Resource<>(dto, categoryLink, selfLink);
    }
}
