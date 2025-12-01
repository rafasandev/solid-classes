package com.example.market_api.core.order.dto;

import java.math.BigDecimal;

import com.example.market_api.core.order.model.enums.OrderStatus;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class OrderResponseDto {
    private OrderStatus orderStatus;
    private String pickupCode;
    private BigDecimal orderTotal;
    private String companyName;
}
