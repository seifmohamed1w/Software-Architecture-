package org.example.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.notification.NotificationService;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;

/**
 * Command pattern implementation for canceling an order.
 * Restores inventory and updates order status.
 */
@RequiredArgsConstructor
@Slf4j
public class CancelOrderCommand implements OrderCommand {

    private final Order order;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void execute() {
        log.info("Executing CancelOrderCommand for order: {}", order.getId());

        // Check if order can be canceled
        if ("CANCELLED".equals(order.getStatus())) {
            log.warn("Order {} is already cancelled", order.getId());
            throw new IllegalStateException("Order is already cancelled");
        }

        if ("SHIPPED".equals(order.getStatus())) {
            log.warn("Cannot cancel shipped order: {}", order.getId());
            throw new IllegalStateException("Cannot cancel a shipped order");
        }

        // Restore inventory if order was placed
        if ("PLACED".equals(order.getStatus()) || "PAID".equals(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                productRepository.findByName(item.getProductName()).ifPresent(product -> {
                    product.increaseStock(item.getQuantity());
                    productRepository.save(product);
                    log.debug("Restored stock for product: {} by {}", item.getProductName(), item.getQuantity());
                });
            }
        }

        // Update order status
        order.updateStatus("CANCELLED");

        // Save the order
        orderRepository.save(order);

        // Notify observers
        notificationService.notifyObservers("Order cancelled: " + order.getId());

        log.info("Order {} cancelled successfully", order.getId());
    }
}
