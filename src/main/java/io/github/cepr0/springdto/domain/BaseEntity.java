package io.github.cepr0.springdto.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;

/**
 * Base class for entity implementations.
 * @author Cepro, 2016-12-10
 */
@Data
@NoArgsConstructor(force = true)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Identifiable<Integer> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private final Integer id;
    
    @Override
    public String toString() {
        return "(id=" + this.getId() + ")";
    }
}
