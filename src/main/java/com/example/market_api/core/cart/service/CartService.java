package com.example.market_api.core.cart.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.cart.ports.CartPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartPort cartPort;

    public Cart getById(UUID id) {
        return cartPort.getById(id);
    }

    public Cart getCartByProfileId(UUID profileId) {
        return cartPort.getCartByProfileId(profileId);
    }

    public Cart save(Cart cart) {
        return cartPort.save(cart);
    }

    public List<Cart> findAll() {
        return cartPort.findAll();
    }

    public void clearCart(Cart cart) {
        if (cart.getItems() != null) {
            cart.getItems().clear();
            cartPort.save(cart);
        }
    }
}
