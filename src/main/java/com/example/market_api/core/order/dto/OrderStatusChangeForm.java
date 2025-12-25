package com.example.market_api.core.order.dto;

import java.util.UUID;

import com.example.market_api.core.order.model.enums.OrderStatus;

import lombok.Getter;

@Getter
public abstract class OrderStatusChangeForm {
    protected UUID orderId;
    protected UUID clientId;
    protected OrderStatus newStatus;
}
