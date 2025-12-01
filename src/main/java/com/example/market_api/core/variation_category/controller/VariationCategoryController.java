package com.example.market_api.core.variation_category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.market_api.core.variation_category.dto.VariationCategoryResponseDto;
import com.example.market_api.core.variation_category.dto.variation_global.VariationCategoryGlobalForm;
import com.example.market_api.core.variation_category.dto.variation_seller.VariationCategorySellerForm;
import com.example.market_api.core.variation_category.service.RegisterVariationCategoryUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/variation-category")
@RequiredArgsConstructor
public class VariationCategoryController {

    private final RegisterVariationCategoryUseCase variationCategoryService;

    @PostMapping("/global")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VariationCategoryResponseDto> createGlobalVariationCategory(@Valid @RequestBody VariationCategoryGlobalForm variationForm) {
        VariationCategoryResponseDto variationCategory = variationCategoryService.registerVariationCategory(variationForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(variationCategory);
    }

    @PostMapping("/seller")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<VariationCategoryResponseDto> createSellerVariationCategory(@Valid @RequestBody VariationCategorySellerForm variationForm) {
        VariationCategoryResponseDto variationCategory = variationCategoryService.registerVariationCategory(variationForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(variationCategory);
    }
    

}
