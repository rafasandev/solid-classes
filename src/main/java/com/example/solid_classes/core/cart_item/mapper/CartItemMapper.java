package com.example.solid_classes.core.cart_item.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.cart_item.dto.CartItemForm;
import com.example.solid_classes.core.cart_item.dto.CartItemResponseDto;
import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.cart_item.model.enums.ReservationStatus;
import com.example.solid_classes.core.product.model.Product;

@Component
public class CartItemMapper {
    
    public CartItem toEntity(CartItemForm cartItemForm, Product product, Cart cart) {
        CartItem newItem = CartItem.builder()
            .product(product)
            .cart(cart)
            .productQuantity(cartItemForm.getItemQuantity())
            .unitPriceSnapshot(product.getBasePrice())
            .status(ReservationStatus.PENDING)
            .build();
        return newItem;
    }

    public CartItemResponseDto toResponseDto(CartItem cartItem) {
        CartItemResponseDto cartItemResponseDto = CartItemResponseDto.builder()
            .productName(cartItem.getProduct().getProductName())
            .build();
        return cartItemResponseDto;
    }
}
