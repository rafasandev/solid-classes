package com.example.solid_classes.core.product_variation.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class ProductVariationResponseDto {
    private UUID id;
    private String variationValue;
    private Double variationPrice;
    private String variationCategoryName;
    private String variationProductName;
}
