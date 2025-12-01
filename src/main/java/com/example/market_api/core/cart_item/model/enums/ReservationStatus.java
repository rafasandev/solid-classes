package com.example.market_api.core.cart_item.model.enums;

public enum ReservationStatus {
    PENDING,      // No carrinho, ainda não reservado
    RESERVED,     // Produto reservado, aguardando retirada
    COMPLETED,    // Retirado
    CANCELLED     // Cancelado
}
