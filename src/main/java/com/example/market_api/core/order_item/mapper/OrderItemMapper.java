package com.example.market_api.core.order_item.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.cart_item.model.CartItem;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.product_variation.model.ProductVariation;

@Component
public class OrderItemMapper {

    public OrderItem toOrderItemSnapshot(CartItem cartItem, Order order, ProductVariation variation) {
        return OrderItem.builder()
                .productId(cartItem.getProductId())
                .productVariationId(cartItem.getProductVariationId())
                .productName(cartItem.getProductName())
                .productVariationValue(variation.getVariationValue())
                .productPrice(cartItem.getUnitPriceSnapshot())
                .variationAdditionalPriceSnapshot(variation.getVariationAdditionalPrice())
                .orderQuantity(cartItem.getItemQuantity())
                .subtotal(cartItem.calculateSubtotal())
                .order(order)
                .build();
    }
}
