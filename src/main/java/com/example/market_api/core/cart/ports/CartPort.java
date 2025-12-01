package com.example.market_api.core.cart.ports;

import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.cart.model.Cart;

public interface CartPort extends NamedCrudPort<Cart>{
    
    Cart getCartByProfileId(UUID id);
}
