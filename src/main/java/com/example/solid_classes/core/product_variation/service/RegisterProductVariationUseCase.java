package com.example.solid_classes.core.product_variation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.service.ProductService;
import com.example.solid_classes.core.product_variation.dto.ProductVariationForm;
import com.example.solid_classes.core.product_variation.dto.ProductVariationResponseDto;
import com.example.solid_classes.core.product_variation.mapper.ProductVariationMapper;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;
import com.example.solid_classes.core.variation_category.service.variation_global.VariationCategoryGlobalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProductVariationUseCase {
    
    private final ProductVariationService productVariationService;
    private final ProductVariationMapper productVariationMapper;
    private final VariationCategoryGlobalService categoryService;
    private final ProductService productService;

    @Transactional
    public ProductVariationResponseDto registerProductVariation(ProductVariationForm variationForm) {
        VariationCategoryEntity category = categoryService.getById(variationForm.getVariationCategoryId());
        Product product = productService.getById(variationForm.getProductId());

        ProductVariation newVariation = productVariationMapper.toEntity(variationForm, category, product);
        newVariation.setStockQuantity(0); // Inicializa a quantidade em estoque como 0

        ProductVariation savedVariation = productVariationService.createProductVariation(newVariation);
        ProductVariationResponseDto variationResponse = productVariationMapper.toResponseDto(savedVariation);
        return variationResponse;
    }
}
