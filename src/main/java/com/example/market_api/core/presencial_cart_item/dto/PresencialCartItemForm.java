package com.example.market_api.core.presencial_cart_item.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PresencialCartItemForm {

    @NotNull(message = "Informe o carrinho presencial")
    private UUID presencialCartId;

    @Positive(message = "Quantidade do item deve ser maior que zero")
    private int itemQuantity;

    @NotNull(message = "Informe a variação do produto")
    private UUID productVariationId;
}
