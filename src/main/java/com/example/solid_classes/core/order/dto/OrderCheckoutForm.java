package com.example.solid_classes.core.order.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderCheckoutForm {
    
    @NotNull(message = "O ID do carrinho é obrigatório")
    private UUID cartId;
    
    @NotNull(message = "O ID do cliente é obrigatório")
    private UUID customerId;
}
