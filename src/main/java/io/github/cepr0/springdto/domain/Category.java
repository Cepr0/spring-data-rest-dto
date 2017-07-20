package io.github.cepr0.springdto.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author Cepro
 * @since 2017-07-20
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@ToString(callSuper = true)
@Entity
public class Category extends BaseEntity {
    
    private final String name;
    
    @OneToMany
    private final Set<Product> products = new HashSet<>();
    
    public Category addProducts(Product... products) {
        this.products.addAll(asList(products));
        return this;
    }
}
