package com.example.market_api.core.order_item.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.order_item.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>{
    
}
