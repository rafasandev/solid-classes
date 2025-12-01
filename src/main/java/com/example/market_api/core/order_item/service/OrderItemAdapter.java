package com.example.market_api.core.order_item.service;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.order_item.ports.OrderItemPort;
import com.example.market_api.core.order_item.repository.jpa.OrderItemRepository;

@Component
public class OrderItemAdapter extends NamedCrudAdapter<OrderItem, OrderItemRepository> implements OrderItemPort{
    
    public OrderItemAdapter(OrderItemRepository orderItemRepository) {
        super(orderItemRepository, "Item de pedido");
    }
}
