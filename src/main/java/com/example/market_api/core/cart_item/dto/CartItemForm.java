package com.example.market_api.core.cart_item.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartItemForm {
    
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer itemQuantity;
    
    @NotNull(message = "O produto é obrigatório")
    private UUID productVariationId;
}
