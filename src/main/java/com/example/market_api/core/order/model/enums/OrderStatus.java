package com.example.market_api.core.order.model.enums;

public enum OrderStatus {
    PENDENTE, // Pedido Gerado
    PEDIDO_CANCELADO, // Pedido cancelado enquanto está pendente (Sem penalização)
    PROCESSANDO, // Pedido está sendo preparado
    CANCELADO_PROCESSANDO, // Pedido cancelado durante o processamento (Penalização de 1 ponto)
    PRONTO_RETIRADA, // Aguardando o cliente buscar
    CANCELADO_RETIRADA, // Pedido cancelado quando estava pronto para retirada antes de expirar (Penalização de 1 ponto)
    COMPLETADO, // Produto entregue dentro do prazo
    EXPIRADO, // Pedido expirado (Penalização de 2 pontos)
    COMPLETADO_EXPIRADO, // Produto entregue após expiração (Mantém penalização de 2 pontos)
    SEM_RETIRADA, // Pedido não retirado pelo cliente (Penalização de 3 pontos)
}