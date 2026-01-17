package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing a customer order in the e-commerce system.
 * Uses JPA annotations for persistence with H2 database.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "items")
@EqualsAndHashCode(exclude = "items")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column
    private String status = "PENDING";

    @Column(nullable = false)
    private double totalAmount;

    @Column
    private String paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "PENDING";
        }
    }

    public Order(String customerName, double totalAmount) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = "PENDING";
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
