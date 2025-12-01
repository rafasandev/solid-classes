package com.example.market_api.core.cart_item.ports;

import java.util.Optional;
import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.cart_item.model.CartItem;

public interface CartItemPort extends NamedCrudPort<CartItem>{
    public Optional<CartItem> getByProductVariationIdAndCartId(UUID productVariationId, UUID cartId);
}
