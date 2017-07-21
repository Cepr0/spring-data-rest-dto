package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.dto.ClassDto;
import io.github.cepr0.springdto.dto.InterfaceDto;
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
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    
    @Query("select c as category, count(p) as quantity from Category c join c.products p group by c")
    List<InterfaceDto> getInterfaceDtos();

    @Query("select new io.github.cepr0.springdto.dto.ClassDto(c, count(p)) from Category c join c.products p group by c")
    List<ClassDto> getClassDtos();

    @RestResource(path="byProductIds", rel="byProductIds")
    @Query("select c from Category c join c.products p on p.id in (?1)")
    List<Category> findByProductIds(@Param("ids") Integer... ids);
}
