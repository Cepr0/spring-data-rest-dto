package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.ProductClassDto;
import io.github.cepr0.springdto.dto.ProductInterfaceDto;
import io.github.cepr0.springdto.repo.ProductRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cepro
 * @since 2017-07-22
 */
@RequiredArgsConstructor
@RepositoryRestController
@RequestMapping("/products")
public class ProductController {
    
    @NonNull
    private final ProductRepo repo;
    
    @NonNull
    private final RepositoryEntityLinks entityLinks;
    
    @GetMapping("/classDto")
    public ResponseEntity<?> classDto() {
        List<ProductClassDto> dtos = repo.getClassDtos();
        
        Link selfRel = entityLinks.linkFor(Category.class).slash("/classDto").withSelfRel();
        return ResponseEntity.ok(new Resources<>(dtos, selfRel));
    }
    
    @GetMapping("/interfaceDto")
    public ResponseEntity<?> interfaceDto() {
        List<ProductInterfaceDto> dtos = repo.getInterfaceDtos();
        List<ProductInterfaceDto> productDtos= new ArrayList<>();
        for (ProductInterfaceDto dto : dtos) {
            ProductInterfaceDto dtoImpl = dto::getName;
            productDtos.add(dtoImpl);
        }
        return ResponseEntity.ok(new Resources<>(productDtos));
    }
}
