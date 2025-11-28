package com.example.solid_classes.core.order.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>{
    
    boolean existsByPickUpcode(String pickUpcode);
}
