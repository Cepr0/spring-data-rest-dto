package io.github.cepr0.springdto.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.Entity;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@ToString(callSuper = true)
@Entity
public class Product extends BaseEntity {

    private final String name;

    @Projection(name = "onlyName", types = {Product.class})
    public interface OnlyName {
        String getName();
    }
}
