package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryClassDto;
import io.github.cepr0.springdto.dto.CategoryInterfaceDto;
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
    private final RepositoryEntityLinks links;

    @NonNull
    private final PagedResourcesAssembler<CategoryInterfaceDto> assembler;

    @GetMapping("/classDto")
    public ResponseEntity<?> classDto() {
        List<CategoryInterfaceDto> dtos = repo.getClassDtos();
        
        Link selfRel = links.linkFor(Category.class).slash("/classDto").withSelfRel();
        Resources<Resource<CategoryInterfaceDto>> resources = Resources.wrap(dtos);
        resources.add(selfRel);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/classDtoPaged")
    public ResponseEntity<?> classDtoPaged(Pageable pageable) {
        Page<CategoryInterfaceDto> dtos = repo.getClassDtos(pageable);

        Link selfRel = links.linkFor(Category.class).slash("/classDtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, selfRel);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/interfaceDto")
    public ResponseEntity<?> interfaceDto() {
        List<CategoryInterfaceDto> dtos = repo.getInterfaceDtos();

        Link selfRel = links.linkFor(Category.class).slash("/interfaceDto").withSelfRel();
        Resources<Resource<CategoryInterfaceDto>> resources = Resources.wrap(dtos);
        resources.add(selfRel);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/interfaceDtoPaged")
    public ResponseEntity<?> interfaceDtoPaged(Pageable pageable) {
        Page<CategoryInterfaceDto> dtos = repo.getClassDtos(pageable);

        Link selfRel = links.linkFor(Category.class).slash("/interfaceDtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, selfRel);
        return ResponseEntity.ok(resources);
    }

    @Bean
    public ResourceProcessor<Resource<CategoryInterfaceDto>> categoryProcessor() {
        return new ResourceProcessor<Resource<CategoryInterfaceDto>>() { // Don't convert to lambda! Wont work!
            @Override
            public Resource<CategoryInterfaceDto> process(Resource<CategoryInterfaceDto> resource) {

                CategoryInterfaceDto content = resource.getContent();
                Link link = links.linkForSingleResource(resource.getContent().getCategory()).withRel("category");

                if (content instanceof CategoryClassDto) {
                    return new Resource<>(content, link);
                } else {
                    resource.add(link);
                    return resource;
                }
            }
        };
    }
}
