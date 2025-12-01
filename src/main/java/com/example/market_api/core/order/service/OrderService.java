package com.example.market_api.core.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order.ports.OrderPort;

import lombok.RequiredArgsConstructor;

/**
 * Service que encapsula o Port e adiciona validações leves.
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderPort orderPort;

    // Métodos CRUD - delegam para o Port
    public Order getById(UUID id) {
        return orderPort.getById(id);
    }

    public Order save(Order order) {
        return orderPort.save(order);
    }

    public List<Order> findAll() {
        return orderPort.findAll();
    }

    public boolean existsByPickupCode(String pickupCode) {
        return orderPort.existsByPickUpcode(pickupCode);
    }
}
