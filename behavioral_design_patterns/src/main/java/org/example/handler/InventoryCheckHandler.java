package org.example.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Chain of Responsibility handler for checking inventory.
 * Validates that all items in the order are in stock.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryCheckHandler extends OrderValidationHandler {

    private final ProductRepository productRepository;

    @Override
    public void validate(Order order) {
        log.info("Checking inventory for order with {} items", order.getItems().size());

        // Check if items are in stock
        for (OrderItem item : order.getItems()) {
            Optional<Product> productOpt = productRepository.findByName(item.getProductName());

            if (productOpt.isEmpty()) {
                log.warn("Product not found: {}", item.getProductName());
                throw new IllegalStateException("Product not found: " + item.getProductName());
            }

            Product product = productOpt.get();
            if (!product.isInStock(item.getQuantity())) {
                log.warn("Insufficient stock for product: {}. Requested: {}, Available: {}",
                        item.getProductName(), item.getQuantity(), product.getStockQuantity());
                throw new IllegalStateException("Insufficient stock for product: " + item.getProductName());
            }

            log.debug("Inventory check passed for product: {} (quantity: {})",
                    item.getProductName(), item.getQuantity());
        }

        log.info("Inventory check passed for all items");

        // Pass to next handler in the chain
        super.validate(order);
    }
}
