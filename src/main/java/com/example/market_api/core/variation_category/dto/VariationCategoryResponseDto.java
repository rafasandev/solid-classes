package com.example.market_api.core.variation_category.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class VariationCategoryResponseDto {
    private UUID id;
    private String variationName;
    private String measureUnit;
    private String variationDescription;
}
