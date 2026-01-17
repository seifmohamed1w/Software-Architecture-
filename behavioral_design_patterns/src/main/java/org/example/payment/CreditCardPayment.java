package org.example.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Strategy pattern implementation for credit card payments.
 */
@Component("creditcard")
@Slf4j
public class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        // Implement credit card payment logic
        log.info("[CREDIT CARD] Processing credit card payment of ${}", amount);

        // Simulate payment processing
        // In a real application, this would integrate with a payment gateway
        // e.g., Stripe, PayPal, Square, etc.

        if (amount <= 0) {
            log.error("Invalid payment amount: {}", amount);
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // Simulate successful payment
        log.info("[CREDIT CARD] Payment of ${} processed successfully", amount);
    }
}
