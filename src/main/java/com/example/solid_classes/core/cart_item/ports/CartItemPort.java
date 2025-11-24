package com.example.solid_classes.core.cart_item.ports;

import java.util.Optional;
import java.util.UUID;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.cart_item.model.CartItem;

public interface CartItemPort extends NamedCrudPort<CartItem>{
    public Optional<CartItem> getByProductIdAndCartId(UUID productId, UUID cartId);
}
