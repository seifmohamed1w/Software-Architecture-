package org.example.handler;

import org.example.model.Order;
import lombok.Setter;

/**
 * Abstract base class for the Chain of Responsibility pattern.
 * Handlers validate orders and pass them to the next handler in the chain.
 */
@Setter
public abstract class OrderValidationHandler {

    protected OrderValidationHandler next;

    /**
     * Validates the order and passes to the next handler if validation passes.
     * 
     * @param order The order to validate
     * @throws IllegalStateException if validation fails
     */
    public void validate(Order order) {
        if (next != null) {
            next.validate(order);
        }
    }
}
