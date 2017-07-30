package io.github.cepr0.springdto.rest;

import io.github.cepr0.springdto.domain.Product;
import io.github.cepr0.springdto.dto.ProductDto;
import io.github.cepr0.springdto.dto.ProductProjection;
import io.github.cepr0.springdto.repo.ProductRepo;
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
 * @since 2017-07-22
 */
@RequiredArgsConstructor
@RepositoryRestController
@RequestMapping("/products")
public class ProductController {
    
    @NonNull private final ProductRepo repo;
    @NonNull private final RepositoryEntityLinks links;
    @NonNull private final PagedResourcesAssembler<ProductProjection> assembler;
    
    @GetMapping("/{id}/dto")
    public ResponseEntity<?> getDto(@PathVariable("id") Integer productId) {
        ProductProjection dto = repo.getDto(productId);
        
        return ResponseEntity.ok(toResource(dto));
    }
    
    @GetMapping("/dto")
    public ResponseEntity<?> getDtos() {
        List<ProductProjection> dtos = repo.getDtos();
        
        Link listSelfLink = links.linkFor(Product.class).slash("/dto").withSelfRel();
        List<?> resources = dtos.stream().map(this::toResource).collect(toList());
    
        return ResponseEntity.ok(new Resources<>(resources, listSelfLink));
    }
    
    @GetMapping("/dtoPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<ProductProjection> dtos = repo.getDtos(pageable);
        
        Link pageSelfLink = links.linkFor(Product.class).slash("/dtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, this::toResource, pageSelfLink);
        
        return ResponseEntity.ok(resources);
    }
    
    private ResourceSupport toResource(ProductProjection projection) {
        ProductDto dto = new ProductDto(projection.getProduct(), projection.getName());
        
        Link productLink = links.linkForSingleResource(projection.getProduct()).withRel("product");
        Link selfLink = links.linkForSingleResource(projection.getProduct()).slash("/dto").withSelfRel();
        
        return new Resource<>(dto, productLink, selfLink);
    }
}
