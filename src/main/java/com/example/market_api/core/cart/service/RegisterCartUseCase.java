package com.example.market_api.core.cart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.cart.dto.CartResponseDto;
import com.example.market_api.core.cart.mapper.CartMapper;
import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterCartUseCase {

    private final CartService cartService;
    private final CartMapper cartMapper;
    
    @Transactional
    public CartResponseDto registerCart(IndividualProfile owner) {
        Cart newCart = cartMapper.toEntity(owner);
        Cart savedCart = cartService.save(newCart);
        CartResponseDto cartResponseDto = cartMapper.toResponseDto(savedCart);
        return cartResponseDto;
    }

    @Transactional
    public Cart createCartOnProfileCreation(IndividualProfile profile) {
        Cart newCart = cartMapper.toEntity(profile);
        Cart savedCart = cartService.save(newCart);
        return savedCart;
    }
}
