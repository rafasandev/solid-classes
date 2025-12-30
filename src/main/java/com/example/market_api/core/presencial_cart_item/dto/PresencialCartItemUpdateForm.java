package com.example.market_api.core.presencial_cart_item.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PresencialCartItemUpdateForm {

    @Positive(message = "Quantidade do item deve ser maior que zero")
    private int itemQuantity;
}
