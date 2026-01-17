package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.command.CancelOrderCommand;
import org.example.command.OrderCommand;
import org.example.command.PlaceOrderCommand;
import org.example.handler.InventoryCheckHandler;
import org.example.handler.OrderValidationHandler;
import org.example.handler.PaymentValidationHandler;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.notification.NotificationService;
import org.example.payment.PaymentStrategy;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service layer for order business logic.
 * Coordinates between controllers, repositories, and design pattern components.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private final InventoryCheckHandler inventoryCheckHandler;
    private final PaymentValidationHandler paymentValidationHandler;
    private final Map<String, PaymentStrategy> paymentStrategies;

    /**
     * Creates a new order after validation through the Chain of Responsibility.
     */
    @Transactional
    public Order createOrder(Order order) {
        log.info("Creating new order for customer: {}", order.getCustomerName());

        // Set up the validation chain (Chain of Responsibility pattern)
        inventoryCheckHandler.setNext(paymentValidationHandler);

        // Validate the order through the chain
        inventoryCheckHandler.validate(order);

        // Save the order
        Order savedOrder = orderRepository.save(order);
        log.debug("Order saved with ID: {}", savedOrder.getId());

        // Notify observers (Observer pattern)
        notificationService.notifyObservers("New order created: " + savedOrder.getId());

        return savedOrder;
    }

    /**
     * Places an order using the Command pattern.
     */
    @Transactional
    public Order placeOrder(Long orderId) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Use Command pattern to place the order
        OrderCommand placeCommand = new PlaceOrderCommand(order, orderRepository, productRepository,
                notificationService);
        placeCommand.execute();

        return orderRepository.save(order);
    }

    /**
     * Cancels an order using the Command pattern.
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Use Command pattern to cancel the order
        OrderCommand cancelCommand = new CancelOrderCommand(order, orderRepository, productRepository,
                notificationService);
        cancelCommand.execute();

        return orderRepository.save(order);
    }

    /**
     * Processes payment using the Strategy pattern.
     */
    @Transactional
    public Order processPayment(Long orderId, String paymentMethod) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Use Strategy pattern to process payment
        PaymentStrategy strategy = paymentStrategies.get(paymentMethod.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown payment method: " + paymentMethod);
        }

        log.info("Processing payment for order {} using {}", orderId, paymentMethod);
        strategy.pay(order.getTotalAmount());

        order.setPaymentMethod(paymentMethod);
        order.updateStatus("PAID");

        // Notify observers about payment
        notificationService.notifyObservers("Payment processed for order: " + orderId);

        return orderRepository.save(order);
    }

    /**
     * Gets an order by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        log.debug("Fetching order with ID: {}", id);
        return orderRepository.findById(id);
    }

    /**
     * Gets all orders.
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        log.debug("Fetching all orders");
        return orderRepository.findAll();
    }

    /**
     * Gets orders by status.
     */
    public List<Order> getOrdersByStatus(String status) {
        log.debug("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status);
    }

    /**
     * Updates order status and notifies observers.
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.updateStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        // Notify observers about status change (Observer pattern)
        notificationService.notifyObservers("Order " + orderId + " status changed to: " + newStatus);

        return updatedOrder;
    }

    /**
     * Adds an item to an existing order.
     */
    @Transactional
    public Order addItemToOrder(Long orderId, OrderItem item) {
        Order order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.addItem(item);
        order.setTotalAmount(order.getTotalAmount() + item.getSubtotal());

        return orderRepository.save(order);
    }
}
