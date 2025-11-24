package com.example.solid_classes.core.cart.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.cart.dto.CartForm;
import com.example.solid_classes.core.cart.dto.CartResponseDto;
import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;

@Component
public class CartMapper {

    public Cart toEntity(CartForm cartForm, IndividualProfile owner) {
        Cart cart = Cart.builder()
                .profile(owner)
                .build();
        return cart;
    }

    public Cart toEntity(IndividualProfile owner) {
        Cart cart = Cart.builder()
                .profile(owner)
                .build();
        return cart;
    }

    public CartResponseDto toResponseDto(Cart cart) {
        CartResponseDto cartResponse = CartResponseDto.builder()
                .profileName(cart.getProfile().getName())
                .build();
        return cartResponse;
    }
}
