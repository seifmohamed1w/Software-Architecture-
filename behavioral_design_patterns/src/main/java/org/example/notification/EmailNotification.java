package org.example.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Observer pattern implementation - Email notification.
 * Receives notifications when order status changes.
 */
@Component
@Slf4j
public class EmailNotification implements Observer {

    @Override
    public void update(String message) {
        // Simulate sending email notification
        log.info("[EMAIL] Sending email notification: {}", message);
        // In a real application, this would integrate with an email service
        // e.g., JavaMailSender, SendGrid, AWS SES, etc.
    }
}
