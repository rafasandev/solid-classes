package com.example.solid_classes.core.product_variation.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductVariationResponseDto {
    private UUID id;
    private String variationValue;
    private BigDecimal variationAdditionalPrice;
    private BigDecimal variationPrice;
    private String variationCategoryName;
    private String variationProductName;
}
