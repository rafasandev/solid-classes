package com.example.market_api.core.order_item.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterOrderItemUseCase {
    
    private final OrderItemService orderItemService;
}
