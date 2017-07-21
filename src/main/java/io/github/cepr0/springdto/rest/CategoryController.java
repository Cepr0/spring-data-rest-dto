package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.dto.ClassDto;
import io.github.cepr0.springdto.dto.InterfaceDto;
import io.github.cepr0.springdto.repo.CategoryRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
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
    
    @GetMapping("/classDto")
    public ResponseEntity<?> classDto() {
        List<ClassDto> dtos = repo.getClassDtos();
        return ResponseEntity.ok(new Resources<>(dtos));
    }

    @GetMapping("/interfaceDto")
    public ResponseEntity<?> interfaceDto() {
        List<InterfaceDto> dtos = repo.getInterfaceDtos();
        return ResponseEntity.ok(new Resources<>(dtos));
    }
}
