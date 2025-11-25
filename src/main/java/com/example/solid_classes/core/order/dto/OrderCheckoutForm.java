package com.example.solid_classes.core.order.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class OrderCheckoutForm {
    private UUID cartId;
    private UUID customerId;
}
