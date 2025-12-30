package com.example.market_api.core.cart_item.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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

import jakarta.validation.Valid;


@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    
    private final RegisterCartItemUseCase registerCartItemUseCase;

    @PostMapping
    @PreAuthorize("hasRole('INDIVIDUAL')")
    public ResponseEntity<CartItemResponseDto> createCartItem(@Valid @RequestBody CartItemForm cartItemForm) {
        CartItemResponseDto newCartitem = registerCartItemUseCase.registerCartItem(cartItemForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCartitem);
    }
    
}
