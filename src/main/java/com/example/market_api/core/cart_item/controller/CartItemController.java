package com.example.market_api.core.cart_item.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.cart_item.dto.CartItemForm;
import com.example.market_api.core.cart_item.dto.CartItemResponseDto;
import com.example.market_api.core.cart_item.service.RegisterCartItemUseCase;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    
    private final RegisterCartItemUseCase registerCartItemUseCase;

    @PostMapping
    public ResponseEntity<CartItemResponseDto> createCartItem(@RequestBody CartItemForm cartItemForm) {
        CartItemResponseDto newCartitem = registerCartItemUseCase.registerCartItem(cartItemForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCartitem);
    }
    
}
