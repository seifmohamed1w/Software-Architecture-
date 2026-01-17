package org.example.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Strategy pattern implementation for PayPal payments.
 */
@Component("paypal")
@Slf4j
public class PayPalPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        log.info("[PAYPAL] Processing PayPal payment of ${}", amount);

        // Simulate PayPal payment processing
        // In a real application, this would integrate with PayPal API

        if (amount <= 0) {
            log.error("Invalid payment amount: {}", amount);
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // Simulate successful payment
        log.info("[PAYPAL] Payment of ${} processed successfully via PayPal", amount);
    }
}
