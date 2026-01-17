package org.example.repository;

import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Product entity CRUD operations.
 * Used by InventoryCheckHandler for inventory validation.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
}
