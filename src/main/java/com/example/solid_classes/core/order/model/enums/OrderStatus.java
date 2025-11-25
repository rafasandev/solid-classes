package com.example.solid_classes.core.order.model.enums;

public enum OrderStatus {
    PENDENTE, // Pedido Gerado
    PAGO, // Pedido Pago
    PRONTO_RETIRADA, // Aguardando o cliente buscar
    COMPLETADO, // Produto entregue
    CANCELADO // Pedido cancelado
}
