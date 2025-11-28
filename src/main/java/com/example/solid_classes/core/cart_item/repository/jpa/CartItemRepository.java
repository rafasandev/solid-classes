package com.example.solid_classes.core.cart_item.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.cart_item.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByProductVariationIdAndCartId(UUID productVariationId, UUID cartId);
}
