package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryAndQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@RepositoryRestResource
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    
    @Query("select c as category, count(p) as quantity from Category c join c.products p group by c")
    List<CategoryAndQuantity> getCategoryAndQuantity();
}
