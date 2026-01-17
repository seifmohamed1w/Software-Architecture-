package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product entity representing items in the product catalog.
 * Used for inventory management in the Chain of Responsibility pattern.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stockQuantity;

    public Product(String name, double price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }

    public void reduceStock(int quantity) {
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + name);
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }
}
