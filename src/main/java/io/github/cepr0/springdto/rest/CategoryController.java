package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.dto.CategoryAndQuantity;
import io.github.cepr0.springdto.repo.CategoryRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

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
    
    @GetMapping("/report")
    public ResponseEntity<?> report() {
        List<CategoryAndQuantity> dtos = repo.getCategoryAndQuantity();
        Resources<CategoryAndQuantity> resources = new Resources<>(dtos);
        return ok(resources);
    }
}
