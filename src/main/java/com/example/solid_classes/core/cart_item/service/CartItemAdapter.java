package com.example.solid_classes.core.cart_item.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.cart_item.ports.CartItemPort;
import com.example.solid_classes.core.cart_item.repository.jpa.CartItemRepository;

@Component
public class CartItemAdapter extends NamedCrudAdapter<CartItem, CartItemRepository> implements CartItemPort {
    
    public CartItemAdapter(CartItemRepository cartItemRepository) {
        super(cartItemRepository, "Item do carrinho");
    }

    @Override
    public Optional<CartItem> getByProductVariationIdAndCartId(UUID productVariationId, UUID cartId) {
        return repository.findByProductVariationIdAndCartId(productVariationId, cartId);
    }
}
