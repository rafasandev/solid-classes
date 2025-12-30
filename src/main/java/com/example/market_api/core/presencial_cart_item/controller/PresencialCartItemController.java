package com.example.market_api.core.presencial_cart_item.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemForm;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemUpdateForm;
import com.example.market_api.core.presencial_cart_item.service.DeletePresencialCartItemUseCase;
import com.example.market_api.core.presencial_cart_item.service.GetPresencialCartItemUseCase;
import com.example.market_api.core.presencial_cart_item.service.RegisterPresencialCartItemUseCase;
import com.example.market_api.core.presencial_cart_item.service.UpdatePresencialCartItemUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/presencial-cart-items")
@RequiredArgsConstructor
public class PresencialCartItemController {

    private final RegisterPresencialCartItemUseCase registerPresencialCartItemUseCase;
    private final UpdatePresencialCartItemUseCase updatePresencialCartItemUseCase;
    private final DeletePresencialCartItemUseCase deletePresencialCartItemUseCase;
    private final GetPresencialCartItemUseCase getPresencialCartItemUseCase;

    @PostMapping
    public ResponseEntity<PresencialCartItemResponseDto> addItem(
            @Valid @RequestBody PresencialCartItemForm presencialCartItemForm) {
        PresencialCartItemResponseDto responseDto = registerPresencialCartItemUseCase
                .registerPresencialCartItem(presencialCartItemForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<PresencialCartItemResponseDto> updateItem(
            @PathVariable UUID itemId,
            @Valid @RequestBody PresencialCartItemUpdateForm updateForm) {
        PresencialCartItemResponseDto responseDto = updatePresencialCartItemUseCase.updatePresencialCartItem(itemId, updateForm);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId) {
        deletePresencialCartItemUseCase.deletePresencialCartItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<PresencialCartItemResponseDto> getItem(@PathVariable UUID itemId) {
        return ResponseEntity.ok(getPresencialCartItemUseCase.getPresencialCartItem(itemId));
    }
}
