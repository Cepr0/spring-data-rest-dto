# Spring Data REST and DTO

_An approach of how to work with [DTO][1] in [Spring Data REST][2] projects_

## Entities

Entities must implement the [Identifiable][3] interface. For example:

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
    @Query("select c as category, count(p) as quantity from Category c join c.products p where c.id = ?1 group by c")
    CategoryProjection getDto(Integer categoryId);
    
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
    public ResponseEntity<?> getDto(@PathVariable("id") Integer categoryId) {
        CategoryProjection dto = repo.getDto(categoryId);
        
        return ResponseEntity.ok(toResource(dto));
    }
    
    /**
    * List of DTO
    */
    @GetMapping("/dto")
    public ResponseEntity<?> getDtos() {
        List<CategoryProjection> dtos = repo.getDtos();
    
        Link listSelfLink = links.linkFor(Category.class).slash("/dto").withSelfRel();
        List<?> resources = dtos.stream().map(this::toResource).collect(toList());

        return ResponseEntity.ok(new Resources<>(resources, listSelfLink));
    }

    /**
    * Paged list of DTO
    */
    @GetMapping("/dtoPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<CategoryProjection> dtos = repo.getDtos(pageable);

        Link pageSelfLink = links.linkFor(Category.class).slash("/dtoPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, this::toResource, pageSelfLink);

        return ResponseEntity.ok(resources);
    }

    private ResourceSupport toResource(CategoryProjection projection) {
        CategoryDto dto = new CategoryDto(projection.getCategory(), projection.getQuantity());
        
        Link categoryLink = links.linkForSingleResource(projection.getCategory()).withRel("category");
        Link selfLink = links.linkForSingleResource(projection.getCategory()).slash("/dto").withSelfRel();
        
        return new Resource<>(dto, categoryLink, selfLink);
    }
}
```

When Projections are received from repository we must make the final transformation from a Projection to DTO 
and 'wrap' it to [ResourceSupport][5] object before sending to the client. 
To do this we use helper method `toResource`: we create a new DTO, create necessary links for this object, 
and then create a new `Resource` with the object and its links.  

## Result

_See the API docs on the [Postman site](https://documenter.getpostman.com/view/788154/spring-data-rest-dto/6mz3FWE)_  

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
[5]: http://docs.spring.io/spring-hateoas/docs/current-SNAPSHOT/api/org/springframework/hateoas/ResourceSupport.html