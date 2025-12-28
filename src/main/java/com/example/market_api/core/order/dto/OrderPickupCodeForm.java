package com.example.market_api.core.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderPickupCodeForm extends OrderStatusChangeForm {
    @NotNull(message = "O código de retirada é obrigatório")
    private String pickupCode;
}
