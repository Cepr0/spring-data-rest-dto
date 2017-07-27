package io.github.cepr0.springdto.dto;

import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Component;

/**
 * @author Cepro
 *         2017-07-21
 */
@Component
public class MyRelProvider implements RelProvider {
    @Override
    public String getItemResourceRelFor(Class<?> type) {
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
