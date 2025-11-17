package com.example.solid_classes.core.product_variation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.product_variation.dto.ProductVariationForm;
import com.example.solid_classes.core.product_variation.dto.ProductVariationResponseDto;
import com.example.solid_classes.core.product_variation.mapper.ProductVariationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProductVariationUseCase {
    
    private final ProductVariationService productVariationService;
    private final ProductVariationMapper productVariationMapper;

    @Transactional
    public ProductVariationResponseDto registerProductVariation(ProductVariationForm variationForm) {

    }
}
