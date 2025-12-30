package com.example.market_api.core.order_item.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.market_api.core.cart_item.model.CartItem;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product_variation.model.ProductVariation;

@Component
public class OrderItemMapper {

    public OrderItem toOrderItemSnapshot(CartItem cartItem, Order order, ProductVariation variation, Product product) {
        BigDecimal finalUnitPrice = calculateFinalUnitPriceSnapshot(variation, product);
        return OrderItem.builder()
                .productId(cartItem.getProductId())
                .productVariationId(cartItem.getProductVariationId())
                .productName(product.getProductName())
                .productVariationValue(variation.getVariationValue())
                .productPrice(product.getBasePrice())
                .variationAdditionalPriceSnapshot(variation.getVariationAdditionalPrice())
                .finalUnitPriceSnapshot(finalUnitPrice)
                .orderQuantity(cartItem.getItemQuantity())
                .subtotal(finalUnitPrice.multiply(BigDecimal.valueOf(cartItem.getItemQuantity())))
                .order(order)
                .build();
    }

    public OrderItem toOrderItemSnapshot(
            PresencialCartItem presencialCartItem,
            Order order,
            Product product,
            ProductVariation variation) {
        BigDecimal finalUnitPrice = calculateFinalUnitPriceSnapshot(variation, product);
        return OrderItem.builder()
                .productId(presencialCartItem.getProductId())
                .productVariationId(presencialCartItem.getProductVariationId())
                .productName(product.getProductName())
                .productVariationValue(variation.getVariationValue())
                .productPrice(product.getBasePrice())
                .variationAdditionalPriceSnapshot(variation.getVariationAdditionalPrice())
                .finalUnitPriceSnapshot(finalUnitPrice)
                .orderQuantity(presencialCartItem.getItemQuantity())
                .subtotal(finalUnitPrice.multiply(BigDecimal.valueOf(presencialCartItem.getItemQuantity())))
                .order(order)
                .build();
    }

    private BigDecimal calculateFinalUnitPriceSnapshot(ProductVariation variation, Product product) {
        return product.getBasePrice().add(variation.getVariationAdditionalPrice());
    }
}
