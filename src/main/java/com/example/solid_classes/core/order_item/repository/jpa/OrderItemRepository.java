package com.example.solid_classes.core.order_item.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.order_item.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>{
    
}
