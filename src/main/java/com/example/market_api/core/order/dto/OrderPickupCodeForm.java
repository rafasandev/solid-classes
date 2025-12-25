package com.example.market_api.core.order.dto;

import lombok.Getter;

@Getter
public class OrderPickupCodeForm extends OrderStatusChangeForm {
    private String pickupCode;
}
