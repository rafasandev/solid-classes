package com.example.market_api.core.order_item.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.core.cart_item.model.CartItem;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order_item.mapper.OrderItemMapper;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.order_item.ports.OrderItemPort;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product_variation.model.ProductVariation;

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

    public OrderItem createOrderItemSnapshot(CartItem cartItem, Order order, ProductVariation variation, Product product) {
        OrderItem orderItem = orderItemMapper.toOrderItemSnapshot(cartItem, order, variation, product);
        return save(orderItem);
    }

    public OrderItem createOrderItemSnapshot(
            PresencialCartItem presencialCartItem,
            Order order,
            Product product,
            ProductVariation variation) {
        OrderItem orderItem = orderItemMapper.toOrderItemSnapshot(presencialCartItem, order, product, variation);
        return save(orderItem);
    }
}
