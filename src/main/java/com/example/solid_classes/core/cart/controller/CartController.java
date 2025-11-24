package com.example.solid_classes.core.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.cart.dto.CartForm;
import com.example.solid_classes.core.cart.dto.CartResponseDto;
import com.example.solid_classes.core.cart.service.RegisterCartUseCase;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final RegisterCartUseCase cartService;
    
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@RequestBody CartForm cartForm) {
        CartResponseDto newCart = cartService.registerCart(cartForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCart);
    }
    
}
