package com.example.market_api.core.presencial_cart_item.mapper;

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
        PresencialCartItem presencialCartItem = PresencialCartItem.builder()
                .itemQuantity(quantity)
                .productVariationId(variation.getId())
                .productId(product.getId())
                .productName(product.getProductName())
                .productVariationValue(variation.getVariationValue())
                .presencialCart(presencialCart)
                .build();

        presencialCartItem.updateSnapshots(product.getBasePrice(), variation.getVariationAdditionalPrice());
        return presencialCartItem;
    }

    public PresencialCartItemResponseDto toResponseDto(PresencialCartItem presencialCartItem) {
        return PresencialCartItemResponseDto.builder()
                .id(presencialCartItem.getId())
                .productId(presencialCartItem.getProductId())
                .productVariationId(presencialCartItem.getProductVariationId())
                .productName(presencialCartItem.getProductName())
                .productVariationValue(presencialCartItem.getProductVariationValue())
                .unitPrice(presencialCartItem.getFinalUnitPriceSnapshot())
                .subtotal(presencialCartItem.getSubtotalSnapshot())
                .quantity(presencialCartItem.getItemQuantity())
                .build();
    }
}
