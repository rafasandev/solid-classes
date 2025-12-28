package com.example.market_api.core.order.dto;

import java.util.UUID;

import com.example.market_api.core.order.model.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class OrderStatusChangeForm {
    @NotNull(message = "O ID do pedido é obrigatório")
    protected UUID orderId;

    @NotNull(message = "O ID do cliente é obrigatório")
    protected UUID clientId;

    @NotNull(message = "O novo status do pedido é obrigatório")
    protected OrderStatus newStatus;
}
