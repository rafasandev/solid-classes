package com.example.solid_classes.core.variation_category.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class VariationCategoryResponseDto {
    private UUID id;
    private String variationName;
    private String measureUnit;
    private String variationDescription;
}
