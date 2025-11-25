package com.example.solid_classes.core.cart_item.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.cart_item.ports.CartItemPort;
import com.example.solid_classes.core.cart_item.repository.CartItemRepository;

@Component
public class CartItemAdapter extends NamedCrudAdapter<CartItem, CartItemRepository> implements CartItemPort {
    
    public CartItemAdapter(CartItemRepository cartItemRepository) {
        super(cartItemRepository, "Item do carrinho");
    }

    @Override
    public Optional<CartItem> getByProductIdAndCartId(UUID productId, UUID cartId) {
        return repository.findByProductIdAndCartId(productId, cartId);
    }
}
