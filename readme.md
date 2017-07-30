# Spring Data REST projects and DTO

An approach of how to work with [DTO][1] in [Spring Data REST][2] projects

## Entities

Entities must be inherited from [Identifiable][3] interface. For example:

```java
@Entity
public class Category implements Identifiable<Integer> {
    
    @Id
    @GeneratedValue
    private final Integer id;
    
    private final String name;
    
    @OneToMany
    private final Set<Product> products = new HashSet<>();
    
    // skipped
}

@Entity
public class Product implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private final Integer id;
    
    private final String name;
    
    // skipped
}
```

## Projections

Make a [projection][4] interface that repository query methods will return:

```java
public interface CategoryProjection {

    Category getCategory();
    Long getQuantity();
}
```

It will be a basement for DTO. DTO will represent a `Category` and the number of `Product`s are belong to it.

## Repository methods

Create methods return the projection: a single one, a list of DTO and a paged list of DTO.

```java
@RepositoryRestResource
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    
    @RestResource(exported = false)
    @Query("select c as category, count(p) as quantity from Category c join c.products p where c = ?1 group by c")
    CategoryProjection getDto(Category category);
    
    @RestResource(exported = false)
    @Query("select c as category, count(p) as quantity from Category c join c.products p group by c")
    List<CategoryProjection> getDtos();
    
    @RestResource(exported = false)
    @Query("select c as category, count(p) as quantity from Category c join c.products p group by c")
    Page<CategoryProjection> getDtos(Pageable pageable);
}
```

## DTO 

Implement DTO from its interface:

```java
@Relation(value = "category", collectionRelation = "categories")
public class CategoryDto implements CategoryProjection {

    private final Category category;
    private final Long quantity;
    
    // skipped
}
``` 

Annotation `Relation` is used when Spring Data REST is rendering the object.

## Controller  

Add custom methods to `RepositoryRestController` that will serve requests of DTO:

```java
@RepositoryRestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired private CategoryRepo repo;
    @Autowired private RepositoryEntityLinks links;
    @Autowired private PagedResourcesAssembler<CategoryProjection> assembler;

    /**
    * Single DTO
    */
    @GetMapping("/{id}/dto")
    public ResponseEntity<?> getDto(@PathVariable("id") Category category) {
        CategoryProjection dto = repo.getDto(category);
        Resource<CategoryProjection> resource = new Resource<>(dto);
        return ResponseEntity.ok(resource);
    }
    
    /**
    * List of DTO
    */
    @GetMapping("/dto")
    public ResponseEntity<?> getDtos() {
        List<CategoryProjection> dtos = repo.getDtos();

        Link selfLink = links.linkFor(Category.class).slash("/dto").withSelfRel();
        Resources<Resource<CategoryProjection>> resources = Resources.wrap(dtos);
        resources.add(selfLink);

        return ResponseEntity.ok(resources);
    }

    /**
    * Paged list of DTO
    */
    @GetMapping("/dtoPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<CategoryProjection> dtos = repo.getDtos(pageable);

        Link selfLink = links.linkFor(Category.class).slash("/dtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, selfLink);

        return ResponseEntity.ok(resources);
    }

    @Bean
    public ResourceProcessor<Resource<CategoryProjection>> categoryDtoProcessor() {
        return new ResourceProcessor<Resource<CategoryProjection>>() { // Don't convert to lambda! Won't work!
            @Override
            public Resource<CategoryProjection> process(Resource<CategoryProjection> resource) {
                CategoryProjection content = resource.getContent();
                
                CategoryDto dto = new CategoryDtoImpl(content.getCategory(), content.getQuantity());
                
                Link categoryLink = links.linkForSingleResource(content.getCategory()).withRel("category");
                Link selfLink = links.linkForSingleResource(content.getCategory()).slash("/dto").withSelfRel();
    
                return new Resource<>(dto, categoryLink, selfLink);
            }
        };
    }
}
```

When Projection is received from repository it must be 'wrapped' to [Resource][5] objects. 
Before sending to a client, controller 'puts' every resource to the appropriate [ResourceProcessor][6] 
to give us the possibility to make additional transformations on it. 

To convert a single Projection to a `Resource` we are using just `Resource` constructor; 
to convert a list of Projection - the static method `wrap` of the class `Resources` and 
to convert a paged list of Projection - `toResource` method of the injected `PagedResourcesAssembler`.

When a resource hits the `ResourceProcessor` we extract a Projection from it, create a new DTO,
create the necessary links for this object, and then create a new `Resource` with the object and its links 
and return it to the controller. Then the controller renders DTO (or the list of DTO) and returns the result to the client.  

## Result

### Singe DTO

    GET http://localhost:8080/api/categories/6/dto
    
```json
{
    "category": {
        "name": "category1"
    },
    "quantity": 3,
    "_links": {
        "category": {
            "href": "http://localhost:8080/api/categories/6"
        },
        "self": {
            "href": "http://localhost:8080/api/categories/6/dto"
        }
    }
}
```

### List of DTO

    GET http://localhost:8080/api/categories/dto
    
```json
{
    "_embedded": {
        "categories": [
            {
                "category": {
                    "name": "category1"
                },
                "quantity": 3,
                "_links": {
                    "category": {
                        "href": "http://localhost:8080/api/categories/6"
                    },
                    "self": {
                        "href": "http://localhost:8080/api/categories/6/dto"
                    }
                }
            },
            {
                "category": {
                    "name": "category2"
                },
                "quantity": 2,
                "_links": {
                    "category": {
                        "href": "http://localhost:8080/api/categories/7"
                    },
                    "self": {
                        "href": "http://localhost:8080/api/categories/7/dto"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/categories/dto"
        }
    }
}
```    

### Paged list of DTO

    GET http://localhost:8080/api/categories/dtoPaged

```json
{
    "_embedded": {
        "categories": [
            {
                "category": {
                    "name": "category1"
                },
                "quantity": 3,
                "_links": {
                    "category": {
                        "href": "http://localhost:8080/api/categories/6"
                    },
                    "self": {
                        "href": "http://localhost:8080/api/categories/6/dto"
                    }
                }
            },
            {
                "category": {
                    "name": "category2"
                },
                "quantity": 2,
                "_links": {
                    "category": {
                        "href": "http://localhost:8080/api/categories/7"
                    },
                    "self": {
                        "href": "http://localhost:8080/api/categories/7/dto"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/categories/dtoPaged"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 2,
        "totalPages": 1,
        "number": 0
    }
}
```    

[1]: https://en.wikipedia.org/wiki/Data_transfer_object
[2]: https://projects.spring.io/spring-data-rest/
[3]: http://docs.spring.io/spring-hateoas/docs/current-SNAPSHOT/api/org/springframework/hateoas/Identifiable.html 
[4]: https://spring.io/blog/2016/05/03/what-s-new-in-spring-data-hopper#projections-on-repository-query-methods
[5]: http://docs.spring.io/spring-hateoas/docs/current-SNAPSHOT/api/org/springframework/hateoas/Resource.html
[6]: http://docs.spring.io/spring-hateoas/docs/current/api/org/springframework/hateoas/ResourceProcessor.html