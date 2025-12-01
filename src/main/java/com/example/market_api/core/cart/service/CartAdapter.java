package com.example.market_api.core.cart.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.cart.ports.CartPort;
import com.example.market_api.core.cart.repository.jpa.CartRepository;

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
