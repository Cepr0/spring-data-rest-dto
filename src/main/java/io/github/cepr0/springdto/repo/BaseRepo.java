package io.github.cepr0.springdto.repo;

import io.github.cepr0.springdto.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Cepro
 * @since 2017-07-20
 */
public interface BaseRepo extends JpaRepository<BaseEntity, Integer> {
}
