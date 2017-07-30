package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.BaseTest;
import io.github.cepr0.springdto.dto.CategoryProjection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Cepro
 *         2017-07-21
 */
public class CategoryRepoTest extends BaseTest {

    @Autowired
    private CategoryRepo repo;

    @Test
    public void getDtos() throws Exception {
        List<CategoryProjection> list = repo.getDtos();
        assertThat(list).isNotNull();
        assertThat(list.size()).isNotZero();
    }
}