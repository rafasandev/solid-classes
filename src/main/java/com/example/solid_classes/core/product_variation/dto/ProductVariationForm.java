package com.example.solid_classes.core.product_variation.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ProductVariationForm {
    private String variationValue;
    private Double variationPrice;
    private UUID variationCategoryId;
}
