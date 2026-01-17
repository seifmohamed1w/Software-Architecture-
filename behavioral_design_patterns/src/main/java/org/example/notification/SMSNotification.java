package org.example.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Observer pattern implementation - SMS notification.
 * Receives notifications when order status changes.
 */
@Component
@Slf4j
public class SMSNotification implements Observer {

    @Override
    public void update(String message) {
        // Simulate sending SMS notification
        log.info("[SMS] Sending SMS notification: {}", message);
        // In a real application, this would integrate with an SMS service
        // e.g., Twilio, AWS SNS, etc.
    }
}
