package com.example.market_api.core.product_variation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.product_variation.dto.ProductVariationForm;
import com.example.market_api.core.product_variation.dto.ProductVariationResponseDto;
import com.example.market_api.core.product_variation.service.GetProductVariationUseCase;
import com.example.market_api.core.product_variation.service.RegisterProductVariationUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product-variation")
@RequiredArgsConstructor
public class ProductVariationController {

    private final RegisterProductVariationUseCase registerProductVariationUseCase;
    private final GetProductVariationUseCase getProductVariationUseCase;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ProductVariationResponseDto> createProductVariation(@Valid @RequestBody ProductVariationForm variationForm) {
        ProductVariationResponseDto newProductVariation = registerProductVariationUseCase.registerProductVariation(variationForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProductVariation);
    }

    @GetMapping
    public ResponseEntity<List<ProductVariationResponseDto>> getAllVariations() {
        return ResponseEntity.ok(getProductVariationUseCase.getAllVariations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariationResponseDto> getVariationById(@PathVariable UUID id) {
        return ResponseEntity.ok(getProductVariationUseCase.getVariationById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductVariationResponseDto>> getVariationsByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(getProductVariationUseCase.getVariationsByProductId(productId));
    }
}
