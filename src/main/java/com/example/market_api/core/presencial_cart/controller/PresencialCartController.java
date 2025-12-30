package com.example.market_api.core.presencial_cart.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.order.dto.OrderResponseDto;
import com.example.market_api.core.presencial_cart.dto.PresencialCartForm;
import com.example.market_api.core.presencial_cart.dto.PresencialCartResponseDto;
import com.example.market_api.core.presencial_cart.service.DeletePresencialCartUseCase;
import com.example.market_api.core.presencial_cart.service.FinalizePresencialCartUseCase;
import com.example.market_api.core.presencial_cart.service.GetPresencialCartUseCase;
import com.example.market_api.core.presencial_cart.service.ListPresencialCartsUseCase;
import com.example.market_api.core.presencial_cart.service.RegisterPresencialCartUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/presencial-carts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COMPANY')")
public class PresencialCartController {

    private final RegisterPresencialCartUseCase registerPresencialCartUseCase;
    private final ListPresencialCartsUseCase listPresencialCartsUseCase;
    private final GetPresencialCartUseCase getPresencialCartUseCase;
    private final DeletePresencialCartUseCase deletePresencialCartUseCase;
    private final FinalizePresencialCartUseCase finalizePresencialCartUseCase;

    @PostMapping
    public ResponseEntity<PresencialCartResponseDto> createPresencialCart(
            @Valid @RequestBody PresencialCartForm presencialCartForm) {
        PresencialCartResponseDto responseDto = registerPresencialCartUseCase
                .registerPresencialCart(presencialCartForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<PresencialCartResponseDto>> listPresencialCarts(Pageable pageable) {
        return ResponseEntity.ok(listPresencialCartsUseCase.getListPresencialCart(pageable));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<PresencialCartResponseDto> getPresencialCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok(getPresencialCartUseCase.getPresencialCart(cartId));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deletePresencialCart(@PathVariable UUID cartId) {
        deletePresencialCartUseCase.deletePresencialCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/finalize")
    public ResponseEntity<OrderResponseDto> finalizePresencialCart(@PathVariable UUID cartId) {
        OrderResponseDto responseDto = finalizePresencialCartUseCase.finalizePresencialCart(cartId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}