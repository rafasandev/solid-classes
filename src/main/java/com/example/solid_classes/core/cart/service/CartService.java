package com.example.solid_classes.core.cart.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.cart.ports.CartPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartPort cartPort;

    public Cart getById(UUID id) {
        return cartPort.getById(id);
    }

    public Cart getByProfileId(UUID id) {
        return cartPort.getCartByProfileId(id);
    }

    public Cart createCart(Cart cart) {
        return cartPort.save(cart);
    }

    public void clearCart(Cart cart) {
        // CORREÇÃO: Implementar limpeza real do carrinho
        if (cart.getItems() != null) {
            cart.getItems().clear();
            cartPort.save(cart);
        }
    }
}
