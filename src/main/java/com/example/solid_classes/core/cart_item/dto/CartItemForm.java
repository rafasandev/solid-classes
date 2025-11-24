package com.example.solid_classes.core.cart_item.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CartItemForm {
    private int itemQuantity;
    private UUID productId;
    private UUID userId;
}
