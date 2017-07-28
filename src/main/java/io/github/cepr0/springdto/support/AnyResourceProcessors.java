package io.github.cepr0.springdto.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

/**
 * Processors of any resource. Dev purposes only.
 * @author Cepro, 2017-01-28
 */
@Slf4j
@Component
public class AnyResourceProcessors {
    
    @Component
    public class SingleResourceProcessor implements ResourceProcessor<Resource<?>> {
    
        @Override
        public Resource<?> process(Resource<?> resource) {
            // LOG.debug("SingleResourceProcessor {}", resource.toString());
            return resource;
        }
    }
    
    @Component
    public class MultiResourceProcessor implements ResourceProcessor<Resources<Resource<?>>> {
    
        @Override
        public Resources<Resource<?>> process(Resources<Resource<?>> resource) {
            // LOG.debug("MultiResourceProcessor {}", resource.toString());
            return resource;
        }
    }
    
    @Component
    public class PagedResourceProcessor implements ResourceProcessor<PagedResources<Resource<?>>> {
    
        @Override
        public PagedResources<Resource<?>> process(PagedResources<Resource<?>> resource) {
            // LOG.debug("PagedResourceProcessor {}", resource.toString());
            return resource;
        }
    }
}
