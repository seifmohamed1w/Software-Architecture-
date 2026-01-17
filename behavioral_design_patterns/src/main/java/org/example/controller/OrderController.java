package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for order management.
 * Provides API endpoints for CRUD operations and order processing.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order.
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("Received request to create order for customer: {}", order.getCustomerName());
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalStateException e) {
            log.error("Failed to create order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all orders.
     * GET /api/orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        log.debug("Fetching all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Get an order by ID.
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        log.debug("Fetching order with ID: {}", id);
        try {
            return orderService.getOrderById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching order {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get orders by status.
     * GET /api/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        log.debug("Fetching orders with status: {}", status);
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    /**
     * Place an order (using Command pattern).
     * POST /api/orders/{id}/place
     */
    @PostMapping("/{id}/place")
    public ResponseEntity<Order> placeOrder(@PathVariable Long id) {
        log.info("Placing order with ID: {}", id);
        try {
            Order placedOrder = orderService.placeOrder(id);
            return ResponseEntity.ok(placedOrder);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Failed to place order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancel an order (using Command pattern).
     * POST /api/orders/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        log.info("Cancelling order with ID: {}", id);
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Failed to cancel order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Process payment for an order (using Strategy pattern).
     * POST /api/orders/{id}/pay
     * Request body: { "paymentMethod": "creditcard" | "paypal" }
     */
    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> processPayment(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String paymentMethod = request.get("paymentMethod");
        log.info("Processing payment for order {} using {}", id, paymentMethod);
        try {
            Order paidOrder = orderService.processPayment(id, paymentMethod);
            return ResponseEntity.ok(paidOrder);
        } catch (IllegalArgumentException e) {
            log.error("Failed to process payment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update order status.
     * PUT /api/orders/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        log.info("Updating order {} status to: {}", id, newStatus);
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update order status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add an item to an order.
     * POST /api/orders/{id}/items
     */
    @PostMapping("/{id}/items")
    public ResponseEntity<Order> addItemToOrder(@PathVariable Long id, @RequestBody OrderItem item) {
        log.info("Adding item to order {}: {}", id, item.getProductName());
        try {
            Order updatedOrder = orderService.addItemToOrder(id, item);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            log.error("Failed to add item to order: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
