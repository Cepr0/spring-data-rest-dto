package io.github.cepr0.springdto.rest;

import java.io.Serializable;

/**
 * @author Cepro
 * @since 2017-07-30
 */
public interface DtoProjection<T, ID extends Serializable> {
    ID getId();
    Class<T> getType();
}
