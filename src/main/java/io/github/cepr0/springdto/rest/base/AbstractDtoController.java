package io.github.cepr0.springdto.rest.base;

import io.github.cepr0.springdto.rest.DtoProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @author Cepro
 * @since 2017-07-30
 */
@RequiredArgsConstructor
@RepositoryRestController
public abstract class AbstractDtoController<P extends DtoProjection, DTO, T, ID extends Serializable> {
    
    @NonNull private final RepositoryEntityLinks links;
    
    protected abstract String getDtoName();
    
    protected abstract P getProjection(ID id);
    
    protected abstract DTO makeDto(P projection);
    
    public ResponseEntity<?> getDto(ID id) {
    
        P projection = getProjection(id);
        return ResponseEntity.ok(toResource(projection));
    }
    
    private ResourceSupport toResource(P projection) {
        
        DTO dto = makeDto(projection);
        ID id = (ID) projection.getId();
        Class<T> type = projection.getType();
        
        Link productLink = links.linkForSingleResource(type, id).withRel(type.getSimpleName().toLowerCase());
        Link selfLink = links.linkForSingleResource(type, id).slash(getDtoName()).withSelfRel();
        
        return new Resource<>(dto, productLink, selfLink);
    }
    
}
