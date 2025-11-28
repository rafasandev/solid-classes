package com.example.solid_classes.core.order_item.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.order_item.mapper.OrderItemMapper;
import com.example.solid_classes.core.order_item.model.OrderItem;
import com.example.solid_classes.core.order_item.ports.OrderItemPort;
import com.example.solid_classes.core.product_variation.model.ProductVariation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemPort orderItemPort;
    private final OrderItemMapper orderItemMapper;

    public OrderItem getById(UUID id) {
        return orderItemPort.getById(id);
    }

    public OrderItem save(OrderItem orderItem) {
        return orderItemPort.save(orderItem);
    }

    public List<OrderItem> findAll() {
        return orderItemPort.findAll();
    }

    public OrderItem createOrderItemSnapshot(CartItem cartItem, Order order, ProductVariation variation) {
        OrderItem orderItem = orderItemMapper.toOrderItemSnapshot(cartItem, order, variation);
        return save(orderItem);
    }
}
