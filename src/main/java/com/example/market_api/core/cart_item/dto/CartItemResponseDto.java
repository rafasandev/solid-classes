package com.example.market_api.core.cart_item.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponseDto {
    private String productName;
    private BigDecimal productUnitPrice;
    private int productQuantity;
}
