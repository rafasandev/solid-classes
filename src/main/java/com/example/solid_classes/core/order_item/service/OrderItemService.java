package com.example.solid_classes.core.order_item.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.order_item.mapper.OrderItemMapper;
import com.example.solid_classes.core.order_item.model.OrderItem;
import com.example.solid_classes.core.order_item.ports.OrderItemPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    
    private final OrderItemPort orderItemPort;
    private final OrderItemMapper orderItemMapper;

    public OrderItem getById(UUID id) {
        return this.orderItemPort.getById(id);
    }

    public OrderItem createOrderItemSnapshot(CartItem cartItem, Order order) {
        OrderItem snapshot = orderItemMapper.toOrderItemSnapshot(cartItem, order);
        return snapshot;
    }
}
