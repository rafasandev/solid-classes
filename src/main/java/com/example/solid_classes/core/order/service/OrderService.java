package com.example.solid_classes.core.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.order.ports.OrderPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderPort orderPort;

    public Order getById(UUID id) {
        return orderPort.getById(id);
    }

    public Order registerOrder(Order newOrder) {
        return orderPort.save(newOrder);
    }

    public boolean existsByPickupCode(String pickupCode) {
        return orderPort.existsByPickUpcode(pickupCode);
    }
}
