package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryClassDto;
import io.github.cepr0.springdto.dto.CategoryInterfaceDto;
import io.github.cepr0.springdto.repo.CategoryRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @NonNull
    private final CategoryRepo repo;

    @NonNull
    private final RepositoryEntityLinks entityLinks;

    @GetMapping("/classDto")
    public ResponseEntity<?> classDto() {
        List<CategoryClassDto> dtos = repo.getClassDtos();

        Link selfRel = entityLinks.linkFor(Category.class).slash("/classDto").withSelfRel();
        return ResponseEntity.ok(new Resources<>(dtos, selfRel));
    }

    @GetMapping("/interfaceDto")
    public ResponseEntity<?> interfaceDto() {
        List<CategoryInterfaceDto> dtos = repo.getInterfaceDtos();
        return ResponseEntity.ok(new Resources<>(dtos));
    }
}
