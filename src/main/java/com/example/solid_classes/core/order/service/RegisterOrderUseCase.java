package com.example.solid_classes.core.order.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterOrderUseCase {
    
    private final OrderService orderService;
}
