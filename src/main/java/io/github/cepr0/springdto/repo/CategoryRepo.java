package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@RepositoryRestResource
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    
    @RestResource(exported = false)
    @Query("select c as category, count(p) as quantity from Category c join c.products p where c = ?1 group by c")
    CategoryDto getDto(Category category);
    
    @RestResource(exported = false)
    @Query("select c as category, count(p) as quantity from Category c join c.products p group by c")
    List<CategoryDto> getDtos();
    
    @RestResource(exported = false)
    @Query("select c as category, count(p) as quantity from Category c join c.products p group by c")
    Page<CategoryDto> getDtos(Pageable pageable);
    
    @Transactional(readOnly = true)
    @RestResource(path="byProductIds", rel="byProductIds")
    @Query("select c from Category c join c.products p on p.id in (?1)")
    List<Category> findByProductIds(@Param("ids") Integer... ids);
}
