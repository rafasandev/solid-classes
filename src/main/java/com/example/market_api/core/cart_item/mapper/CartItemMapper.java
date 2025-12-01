package com.example.market_api.core.cart_item.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.cart_item.dto.CartItemForm;
import com.example.market_api.core.cart_item.dto.CartItemResponseDto;
import com.example.market_api.core.cart_item.model.CartItem;
import com.example.market_api.core.cart_item.model.enums.ReservationStatus;
import com.example.market_api.core.product.model.Product;

@Component
public class CartItemMapper {

    public CartItem toEntity(CartItemForm cartItemForm, Product product, Cart cart) {
        CartItem newItem = CartItem.builder()
                .productVariationId(cartItemForm.getProductVariationId())
                .productId(product.getId())
                .productName(product.getProductName())
                .itemQuantity(cartItemForm.getItemQuantity())
                .unitPriceSnapshot(product.getBasePrice())
                .status(ReservationStatus.PENDING)
                .cart(cart)
                .build();
        return newItem;
    }

    public CartItemResponseDto toResponseDto(CartItem cartItem) {
        CartItemResponseDto cartItemResponseDto = CartItemResponseDto.builder()
                .productName(cartItem.getProductName())
                .build();
        return cartItemResponseDto;
    }
}
