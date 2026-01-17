package org.example.notification;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer pattern subject - NotificationService.
 * Manages observers and notifies them of order status changes.
 */
@Service
@Slf4j
public class NotificationService {

    private final List<Observer> observers = new ArrayList<>();

    @Autowired(required = false)
    private List<Observer> autowiredObservers;

    @PostConstruct
    public void init() {
        // Auto-register all Observer beans
        if (autowiredObservers != null) {
            observers.addAll(autowiredObservers);
            log.info("Registered {} notification observers", observers.size());
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        log.debug("Added observer: {}", observer.getClass().getSimpleName());
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
        log.debug("Removed observer: {}", observer.getClass().getSimpleName());
    }

    public void notifyObservers(String message) {
        log.info("Notifying {} observers with message: {}", observers.size(), message);
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
