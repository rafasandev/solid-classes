package com.example.solid_classes.core.cart.ports;

import java.util.UUID;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.cart.model.Cart;

public interface CartPort extends NamedCrudPort<Cart>{
    
    Cart getCartByProfileId(UUID id);
}
