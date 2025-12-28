package com.example.market_api.core.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.order.dto.OrderPickupCodeForm;
import com.example.market_api.core.order.dto.OrderResponseDto;
import com.example.market_api.core.order.dto.OrderStatusChangeForm;
import com.example.market_api.core.order.service.use_case.CheckoutOrderUseCase;
import com.example.market_api.core.order.service.use_case.OrderStatusChangeSpecificUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutOrderUseCase checkoutService;
    private final OrderStatusChangeSpecificUseCase setOrderStatusSpecific;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('INDIVIDUAL')")
    public ResponseEntity<List<OrderResponseDto>> orderChechout() {
        List<OrderResponseDto> orders = checkoutService.checkout();
        return ResponseEntity.status(HttpStatus.CREATED).body(orders);
    }
    
    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@RequestBody @Valid OrderStatusChangeForm statusChangeForm) {
        OrderResponseDto updatedOrder = setOrderStatusSpecific.updateOrderStatusGeneral(statusChangeForm);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }
    
    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/status/checkout")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@RequestBody @Valid OrderPickupCodeForm statusChangeForm) {
        OrderResponseDto updatedOrder = setOrderStatusSpecific.updateOrderStatusPickup(statusChangeForm);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

}
