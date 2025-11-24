package com.example.solid_classes.core.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID>{
    Optional<Cart> findByProfileId(UUID id);
}
