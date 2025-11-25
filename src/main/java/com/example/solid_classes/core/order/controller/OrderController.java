package com.example.solid_classes.core.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.solid_classes.core.order.dto.OrderCheckoutForm;
import com.example.solid_classes.core.order.dto.OrderResponseDto;
import com.example.solid_classes.core.order.service.CheckoutOrderUseCase;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutOrderUseCase checkoutService;
    
    @PostMapping("/checkout")
    public ResponseEntity<List<OrderResponseDto>> orderChechout(@Valid @RequestBody OrderCheckoutForm checkoutData) {
        List<OrderResponseDto> orders = checkoutService.checkout(checkoutData);
        return ResponseEntity.status(HttpStatus.CREATED).body(orders);
    }
    
}
