package com.example.solid_classes.core.order.dto;

import com.example.solid_classes.core.order.model.enums.OrderStatus;

import lombok.Builder;

@Builder
public class OrderResponseDto {
    private OrderStatus orderStatus;
    private String pickupCode;
}
