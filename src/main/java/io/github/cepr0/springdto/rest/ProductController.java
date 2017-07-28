package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Product;
import io.github.cepr0.springdto.dto.ProductDto;
import io.github.cepr0.springdto.dto.ProductDtoImpl;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @NonNull private final ProductRepo repo;
    @NonNull private final RepositoryEntityLinks links;
    @NonNull private final PagedResourcesAssembler<ProductDto> assembler;
    
    @GetMapping("/{id}/dto")
    public ResponseEntity<?> getDto(@PathVariable("id") Product product) {
        ProductDto dto = repo.getDto(product);
        Resource<ProductDto> resource = new Resource<>(dto);
        return ResponseEntity.ok(resource);
    }
    
    @GetMapping("/dto")
    public ResponseEntity<?> getDtos() {
        List<ProductDto> dtos = repo.getDtos();
        
        Link selfLink = links.linkFor(Product.class).slash("/dto").withSelfRel();
        Resources<Resource<ProductDto>> resources = Resources.wrap(dtos);
        resources.add(selfLink);
        
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/dtoPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<ProductDto> dtos = repo.getDtos(pageable);
        
        Link selfLink = links.linkFor(Product.class).slash("/dtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, selfLink);
        
        return ResponseEntity.ok(resources);
    }
    
    @Bean
    public ResourceProcessor<Resource<ProductDto>> productDtoProcessor() {
        return new ResourceProcessor<Resource<ProductDto>>() { // Don't convert to lambda! Wont work!
            @Override
            public Resource<ProductDto> process(Resource<ProductDto> resource) {
                ProductDto content = resource.getContent();
                
                ProductDtoImpl dto = new ProductDtoImpl(content.getProduct(), content.getName());
                
                Link productLink = links.linkForSingleResource(content.getProduct()).withRel("product");
                Link selfLink = links.linkForSingleResource(content.getProduct()).slash("/dto").withSelfRel();
                
                return new Resource<>(dto, productLink, selfLink);
            }
        };
    }
}
