package com.example.solid_classes.core.product_variation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.product_variation.dto.ProductVariationForm;
import com.example.solid_classes.core.product_variation.dto.ProductVariationResponseDto;
import com.example.solid_classes.core.product_variation.mapper.ProductVariationMapper;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.variation_category.model.VariationCategory;
import com.example.solid_classes.core.variation_category.service.VariationCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProductVariationUseCase {
    
    private final ProductVariationService productVariationService;
    private final ProductVariationMapper productVariationMapper;
    private final VariationCategoryService categoryService;

    @Transactional
    public ProductVariationResponseDto registerProductVariation(ProductVariationForm variationForm) {
        VariationCategory category = categoryService.getById(variationForm.getVariationCategoryId());

        ProductVariation newVariation = productVariationMapper.toEntity(variationForm, category);
        ProductVariation savedVariation = productVariationService.createProductVariation(newVariation);
        ProductVariationResponseDto variationResponse = productVariationMapper.toResponseDto(savedVariation);
        return variationResponse;
    }
}
