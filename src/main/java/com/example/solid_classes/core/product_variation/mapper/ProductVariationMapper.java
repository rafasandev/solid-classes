package com.example.solid_classes.core.product_variation.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product_variation.dto.ProductVariationForm;
import com.example.solid_classes.core.product_variation.dto.ProductVariationResponseDto;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;

@Component
public class ProductVariationMapper {
    public ProductVariation toEntity(ProductVariationForm variationForm, VariationCategoryEntity category, Product product) {
        ProductVariation newVariation = ProductVariation.builder()
            .variationValue(variationForm.getVariationValue())
            .valueType(variationForm.getValueType())
            .variationAdditionalPrice(variationForm.getVariationAdditionalPrice())
            .variationCategory(category)
            .product(product)
            .stockQuantity(0)
            .build();
        return newVariation;
    }

    public ProductVariationResponseDto toResponseDto(ProductVariation variation) {
        return ProductVariationResponseDto.builder()
            .id(variation.getId())
            .variationValue(variation.getVariationValue())
            .variationPrice(variation.getVariationAdditionalPrice())
            .variationCategoryName(variation.getVariationCategory().getName())
            .variationProductName(variation.getProduct().getProductName())
            .build();
    }
}
