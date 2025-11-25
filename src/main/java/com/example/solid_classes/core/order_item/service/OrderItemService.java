package com.example.solid_classes.core.order_item.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.order_item.model.OrderItem;
import com.example.solid_classes.core.order_item.ports.OrderItemPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    
    private final OrderItemPort orderItemPort;

    public OrderItem getById(UUID id) {
        return this.orderItemPort.getById(id);
    }
}
