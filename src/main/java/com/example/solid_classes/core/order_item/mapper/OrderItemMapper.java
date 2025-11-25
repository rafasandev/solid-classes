package com.example.solid_classes.core.order_item.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.order_item.model.OrderItem;

@Component
public class OrderItemMapper {
    
    public OrderItem toOrderItemSnapshot(CartItem cartItem, Order order) {
        OrderItem orderItem = OrderItem.builder()
            .productName(cartItem.getProduct().getProductName())
            .productPrice(cartItem.getUnitPriceSnapshot())
            .productQuantity(cartItem.getProductQuantity())
            .subtotal(cartItem.calculateSubtotal())
            .product(cartItem.getProduct())
            .order(order)
            .build();

        return orderItem;
    }
}
