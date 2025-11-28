package com.example.solid_classes.core.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.order.dto.OrderResponseDto;
import com.example.solid_classes.core.order.service.CheckoutOrderUseCase;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutOrderUseCase checkoutService;
    
    @PostMapping("/checkout")
    @PreAuthorize("hasRole('INDIVIDUAL')")
    public ResponseEntity<List<OrderResponseDto>> orderChechout() {
        List<OrderResponseDto> orders = checkoutService.checkout();
        return ResponseEntity.status(HttpStatus.CREATED).body(orders);
    }
    
}
