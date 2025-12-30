package com.example.market_api.core.presencial_cart_item.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product_variation.model.ProductVariation;

@Component
public class PresencialCartItemMapper {

    public PresencialCartItem toEntity(
            PresencialCart presencialCart,
            Product product,
            ProductVariation variation,
            int quantity) {
        return PresencialCartItem.builder()
                .itemQuantity(quantity)
                .productVariationId(variation.getId())
                .productId(product.getId())
                .presencialCart(presencialCart)
                .build();
    }

    public PresencialCartItemResponseDto toResponseDto(
            PresencialCartItem presencialCartItem,
            Product product,
            ProductVariation variation) {
        BigDecimal unitPrice = calculateUnitPrice(product, variation);
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(presencialCartItem.getItemQuantity()));

        return PresencialCartItemResponseDto.builder()
                .id(presencialCartItem.getId())
                .productId(product.getId())
                .productVariationId(variation.getId())
                .productName(product.getProductName())
                .productVariationValue(variation.getVariationValue())
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .quantity(presencialCartItem.getItemQuantity())
                .build();
    }

    private BigDecimal calculateUnitPrice(Product product, ProductVariation variation) {
        return product.getBasePrice().add(variation.getVariationAdditionalPrice());
    }
}
