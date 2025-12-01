package com.example.market_api.core.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.market_api.core.cart_item.model.CartItem;

/**
 * Componente responsável por cálculos relacionados a pedidos.
 * Segue SRP - responsabilidade única de cálculos.
 */
@Component
public class OrderCalculator {

    /**
     * Calcula o total do pedido somando os subtotais de todos os itens.
     * 
     * @param items Lista de itens do pedido
     * @return Total do pedido
     */
    public BigDecimal calculateOrderTotal(List<CartItem> items) {
        return items.stream()
            .map(CartItem::calculateSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
