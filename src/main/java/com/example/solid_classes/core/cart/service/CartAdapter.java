package com.example.solid_classes.core.cart.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.cart.ports.CartPort;
import com.example.solid_classes.core.cart.repository.CartRepository;

@Component
public class CartAdapter extends NamedCrudAdapter<Cart, CartRepository> implements CartPort {

    public CartAdapter(CartRepository cartRepository) {
        super(cartRepository, "Carrinho");
    }

    @Override
    public Cart getCartByProfileId(UUID id) {
        return repository.findByProfileId(id).orElseThrow(this::throwEntityNotFound);
    }
    
}
