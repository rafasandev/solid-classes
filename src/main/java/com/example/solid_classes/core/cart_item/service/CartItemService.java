package com.example.solid_classes.core.cart_item.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.cart_item.ports.CartItemPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemPort cartItemPort;

    public CartItem getById(UUID id) {
        return cartItemPort.getById(id);
    }

    public Optional<CartItem> getByProductIdAndCartId(UUID productId, UUID cartId) {
        return cartItemPort.getByProductIdAndCartId(productId, cartId);
    }

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemPort.save(cartItem);
    }
}
