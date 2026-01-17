package org.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application Entry Point.
 * E-Commerce Order Processing System demonstrating behavioral design patterns.
 */
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BehavioralPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehavioralPatternsApplication.class, args);
    }

    /**
     * Initialize sample data for testing.
     */
    @Bean
    CommandLineRunner initData(ProductRepository productRepository) {
        return args -> {
            log.info("Initializing sample product data...");

            // Create sample products
            productRepository.save(new Product("Laptop", 999.99, 10));
            productRepository.save(new Product("Smartphone", 699.99, 25));
            productRepository.save(new Product("Headphones", 149.99, 50));
            productRepository.save(new Product("Tablet", 449.99, 15));
            productRepository.save(new Product("Smartwatch", 299.99, 30));

            log.info("Sample data initialized. {} products created.", productRepository.count());
            log.info("Application ready! Access H2 Console at: http://localhost:8080/h2-console");
            log.info("API endpoints available at: http://localhost:8080/api/orders");
        };
    }
}
