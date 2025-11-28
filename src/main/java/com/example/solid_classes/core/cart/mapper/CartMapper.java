package com.example.solid_classes.core.cart.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.cart.dto.CartResponseDto;
import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;

@Component
public class CartMapper {

    public Cart toEntity(IndividualProfile owner) {
        Cart cart = Cart.builder()
                .profile(owner)
                .build();
        return cart;
    }

    public CartResponseDto toResponseDto(Cart cart) {
        CartResponseDto cartResponse = CartResponseDto.builder()
                .clientName(cart.getProfile().getName())
                .build();
        return cartResponse;
    }
}
