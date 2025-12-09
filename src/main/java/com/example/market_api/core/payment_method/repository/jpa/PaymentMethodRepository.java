package com.example.market_api.core.payment_method.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.payment_method.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    
}
