package com.example.solid_classes.core.order_item.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterOrderItemUseCase {
    
    private final OrderItemService orderItemService;
}
