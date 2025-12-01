package com.example.market_api.core.order.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>{
    
    boolean existsByPickUpcode(String pickUpcode);
}
