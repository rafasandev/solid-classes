package com.example.solid_classes.core.product_variation.dto;

import java.util.UUID;

import com.example.solid_classes.core.product_variation.model.abs.VariationValueType;

import lombok.Getter;

@Getter
public class ProductVariationForm {
    private String variationValue;
    private VariationValueType valueType;
    private Double variationAdditionalPrice;
    private UUID variationCategoryId;
    private UUID productId;
}
