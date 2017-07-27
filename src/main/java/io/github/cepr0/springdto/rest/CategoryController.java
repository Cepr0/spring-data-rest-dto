package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryClassDto;
import io.github.cepr0.springdto.dto.CategoryInterfaceDto;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Cepro
 * @since 2017-07-21
 */
// @Configuration
@RequiredArgsConstructor
@RepositoryRestController
@RequestMapping("/categories")
public class CategoryController {
    
    @NonNull
    private final CategoryRepo repo;
    
    @NonNull
    private final RepositoryEntityLinks entityLinks;
    
    @NonNull
    private final PagedResourcesAssembler<CategoryClassDto> assembler;
    
    @GetMapping("/classDto")
    public ResponseEntity<?> classDto() {
        List<CategoryClassDto> dtos = repo.getClassDtos();
        
        Link selfRel = entityLinks.linkFor(Category.class).slash("/classDto").withSelfRel();
        // Resources<Resource<CategoryClassDto>> resources = Resources.wrap(dtos);
        Resources<CategoryClassDto> resources = new Resources<>(dtos);
        resources.add(selfRel);
        
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/classDtoPaged")
    public ResponseEntity<?> classDtoPaged(Pageable pageable) {
        Page<CategoryClassDto> dtos = repo.getClassDtos(pageable);
        
        Link selfRel = entityLinks.linkFor(Category.class).slash("/classDtoPaged").withSelfRel();
        // Resources<Resource<CategoryClassDto>> resources = PagedResources
        //         .wrap(dtos.getContent(),
        //                 new PagedResources.PageMetadata(
        //                         pageable.getPageSize(),
        //                         pageable.getPageNumber(),
        //                         dtos.getTotalElements(),
        //                         dtos.getTotalPages()));
        // resources.add(selfRel);
        
        PagedResources<?> resources = assembler.toResource(dtos, /*this::toResource, */selfRel);
        return ResponseEntity.ok(resources);
    }
    
    
    @GetMapping("/interfaceDto")
    public ResponseEntity<?> interfaceDto() {
        List<CategoryInterfaceDto> dtos = repo.getInterfaceDtos();
        return ResponseEntity.ok(new Resources<>(dtos));
    }
    
    // private ResourceSupport toResource(CategoryClassDto dto) {
    //     return new Resource<>(dto,
    //             entityLinks.linkForSingleResource(dto.getCategory()).withRel("category"));
    // }
    
    // @Bean
    // public ResourceProcessor<Resource<CategoryClassDto>> resourceProcessor() {
    //     return new ResourceProcessor<Resource<CategoryClassDto>>() {
    //         @Override
    //         public Resource<CategoryClassDto> process(Resource<CategoryClassDto> resource) {
    //             resource.add(entityLinks.linkForSingleResource(resource.getContent().getCategory()).withRel("category"));
    //             return resource;
    //         }
    //     };
    // }
    
    // @Component
    // public class CategoryClassDtoResourceProcessor implements ResourceProcessor<Resource<CategoryClassDto>> {
    //
    //     @Override
    //     public Resource<CategoryClassDto> process(Resource<CategoryClassDto> resource) {
    //         resource.add(entityLinks.linkForSingleResource(resource.getContent().getCategory()).withRel("category"));
    //         return resource;
    //     }
    // }
}
