package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.springframework.stereotype.Component;

/**
 * Chain of Responsibility handler for validating payment details.
 * Validates that payment method is set and total amount is valid.
 */
@Component
@Slf4j
public class PaymentValidationHandler extends OrderValidationHandler {

    @Override
    public void validate(Order order) {
        log.info("Validating payment details for order");

        // Validate payment details
        if (order.getTotalAmount() <= 0) {
            log.warn("Invalid order total amount: {}", order.getTotalAmount());
            throw new IllegalStateException("Order total amount must be greater than 0");
        }

        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
            log.warn("Customer name is required");
            throw new IllegalStateException("Customer name is required");
        }

        log.info("Payment validation passed. Total amount: ${}", order.getTotalAmount());

        // Pass to next handler in the chain
        super.validate(order);
    }
}
