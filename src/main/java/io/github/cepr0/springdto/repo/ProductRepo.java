package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.domain.Product;
import io.github.cepr0.springdto.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@RepositoryRestResource
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @RestResource(path="byName", rel="byName")
    Page<Product> findByNameContainingIgnoreCase(@Param("name") String namePart, Pageable pageable);
    
    @RestResource(exported = false)
    @Query("select p as product, p.name as name from Product p where p = ?1")
    ProductDto getDto(Product product);
    
    @RestResource(exported = false)
    @Query("select p as product, p.name as name from Product p")
    List<ProductDto> getDtos();
    
    @RestResource(exported = false)
    @Query("select p as product, p.name as name from Product p")
    Page<ProductDto> getDtos(Pageable pageable);
}
