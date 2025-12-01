package com.example.market_api.core.cart.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID>{
    Optional<Cart> findByProfileId(UUID id);
}
