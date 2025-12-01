package com.example.market_api.core.product_variation.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product_variation.dto.ProductVariationForm;
import com.example.market_api.core.product_variation.dto.ProductVariationResponseDto;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.variation_category.model.VariationCategoryEntity;

@Component
public class ProductVariationMapper {
    public ProductVariation toEntity(
            ProductVariationForm variationForm,
            VariationCategoryEntity category) {
        ProductVariation newVariation = ProductVariation.builder()
                .variationValue(variationForm.getVariationValue())
                .valueType(variationForm.getValueType())
                .variationAdditionalPrice(variationForm.getVariationAdditionalPrice())
                .variationCategoryId(category.getId())
                .productId(variationForm.getProductId())
                .stockQuantity(variationForm.getStockQuantity())
                .build();
        return newVariation;
    }

    public ProductVariationResponseDto toResponseDto(
            ProductVariation variation,
            VariationCategoryEntity variationCategory,
            Product product) {
        return ProductVariationResponseDto.builder()
                .id(variation.getId())
                .variationValue(variation.getVariationValue())
                .variationPrice(variation.getVariationAdditionalPrice())
                .variationAdditionalPrice(variation.getVariationAdditionalPrice())
                .variationCategoryName(variationCategory.getName())
                .variationProductName(product.getProductName())
                .build();
    }
}
