package com.example.market_api.core.presencial_cart_item.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresencialCartItemResponseDto {
    private UUID id;
    private UUID productId;
    private UUID productVariationId;
    private String productName;
    private String productVariationValue;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private int quantity;
}
