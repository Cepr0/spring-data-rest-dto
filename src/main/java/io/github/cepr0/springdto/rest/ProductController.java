package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.ProductClassDto;
import io.github.cepr0.springdto.dto.ProductInterfaceDto;
import io.github.cepr0.springdto.repo.ProductRepo;
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
import org.springframework.web.bind.annotation.RequestMapping;

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
    private final RepositoryEntityLinks links;

    @NonNull
    private final PagedResourcesAssembler<ProductClassDto> assembler;
    
    @GetMapping("/classDto")
    public ResponseEntity<?> classDto() {
        List<ProductClassDto> dtos = repo.getClassDtos();
        
        Link selfRel = links.linkFor(Category.class).slash("/classDto").withSelfRel();
        Resources<Resource<ProductClassDto>> resources = Resources.wrap(dtos);
        resources.add(selfRel);

        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/classDtoPaged")
    public ResponseEntity<?> classDtoPaged(Pageable pageable) {
        Page<ProductClassDto> dtos = repo.getClassDtos(pageable);

        Link selfRel = links.linkFor(Category.class).slash("/classDtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, selfRel);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/interfaceDto")
    public ResponseEntity<?> interfaceDto() {
        List<ProductInterfaceDto> dtos = repo.getInterfaceDtos();
        return ResponseEntity.ok(new Resources<>(dtos));
    }

    @Bean
    public ResourceProcessor<Resource<ProductClassDto>> productProcessor() {
        return new ResourceProcessor<Resource<ProductClassDto>>() { // Don't convert to lambda! Wont work!
            @Override
            public Resource<ProductClassDto> process(Resource<ProductClassDto> resource) {
                resource.add(links.linkForSingleResource(resource.getContent().getProduct()).withRel("product"));
                return resource;
            }
        };
    }
}
