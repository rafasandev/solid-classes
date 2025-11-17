package com.example.solid_classes.core.product_variation.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.product_variation.dto.ProductVariationForm;
import com.example.solid_classes.core.product_variation.dto.ProductVariationResponseDto;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.variation_category.model.VariationCategory;

@Component
public class ProductVariationMapper {
    public ProductVariation toEntity(ProductVariationForm variationForm, VariationCategory category) {
        ProductVariation newVariation = ProductVariation.builder()
            .variationValue(variationForm.getVariationValue())
            .variationPrice(variationForm.getVariationPrice())
            .variationCategory(category)
            .build();
        return newVariation;
    }

    public ProductVariationResponseDto toResponseDto(ProductVariation variation) {
        return ProductVariationResponseDto.builder()
            .id(variation.getId())
            .variationValue(variation.getVariationValue())
            .variationPrice(variation.getVariationPrice())
            .variationCategoryName(variation.getVariationCategory().getVariationName())
            .build();
    }
}
