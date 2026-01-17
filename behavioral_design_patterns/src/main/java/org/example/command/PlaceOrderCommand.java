package org.example.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.example.notification.NotificationService;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;

/**
 * Command pattern implementation for placing an order.
 * Executes the order placement logic including inventory reduction.
 */
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderCommand implements OrderCommand {

    private final Order order;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void execute() {
        log.info("Executing PlaceOrderCommand for order: {}", order.getId());

        // Implement order placement logic
        // 1. Reduce product inventory
        for (OrderItem item : order.getItems()) {
            productRepository.findByName(item.getProductName()).ifPresent(product -> {
                product.reduceStock(item.getQuantity());
                productRepository.save(product);
                log.debug("Reduced stock for product: {} by {}", item.getProductName(), item.getQuantity());
            });
        }

        // 2. Update order status
        order.updateStatus("PLACED");

        // 3. Save the order
        orderRepository.save(order);

        // 4. Notify observers
        notificationService.notifyObservers("Order placed successfully: " + order.getId());

        log.info("Order {} placed successfully", order.getId());
    }
}
