package io.github.cepr0.springdto;

import io.github.cepr0.springdto.domain.Category;
import io.github.cepr0.springdto.domain.Product;
import io.github.cepr0.springdto.repo.BaseRepo;
import org.h2.tools.Server;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Bean
    public ApplicationRunner demoData(BaseRepo repo) {
        return (ApplicationArguments args) -> {
            
            List<Product> products = repo.save(asList(
                    new Product("product1"),
                    new Product("product2"),
                    new Product("product3"),
                    new Product("product4"),
                    new Product("product5")
            ));
            
            repo.save(asList(
                    new Category("category1")
                            .addProducts(products.get(0), products.get(1), products.get(2)),
                    new Category("category2")
                            .addProducts(products.get(3), products.get(4))
            ));
        };
    }
}
