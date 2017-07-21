package io.github.cepr0.springdto.repo;

import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.core.Relation;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author Cepro
 *         2017-07-21
 */
@Component
public class MyRelProvider implements RelProvider {
    @Override
    public String getItemResourceRelFor(Class<?> type) {
        Class<?> iFace = type.getInterfaces()[0];
        if (iFace != null) {
            Annotation[] annotations = iFace.getAnnotations();
            Annotation relation = Arrays.stream(annotations)
                    .filter(annotation -> annotation.annotationType().equals(Relation.class))
                    .findFirst().orElse(null);
        }
        return "category";
    }

    @Override
    public String getCollectionResourceRelFor(Class<?> type) {
        return "categories";
    }

    @Override
    public boolean supports(Class<?> delimiter) {
        return true;
    }
}
