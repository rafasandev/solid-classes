package com.example.market_api.core.cart.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.cart.dto.CartResponseDto;
import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

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
