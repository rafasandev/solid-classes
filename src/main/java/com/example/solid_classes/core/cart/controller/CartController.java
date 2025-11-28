package com.example.solid_classes.core.cart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    // Criação do Cart deve ser feita automaticamente ao criar o perfil do usuário.

    // private final RegisterCartUseCase cartService;
    
    // @PostMapping
    // @PreAuthorize("hasRole('INDIVIDUAL')")
    // public ResponseEntity<CartResponseDto> createCart(@RequestBody CartForm cartForm) {
    //     CartResponseDto newCart = cartService.registerCart(cartForm);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(newCart);
    // }
    
}
