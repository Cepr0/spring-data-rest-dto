package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@RepositoryRestResource
public interface ProductRepo extends JpaRepository<Product, Integer> {
}
