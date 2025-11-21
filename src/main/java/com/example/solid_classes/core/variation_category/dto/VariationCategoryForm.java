package com.example.solid_classes.core.variation_category.dto;

import com.example.solid_classes.core.variation_category.model.enums.MeasureUnit;

import lombok.Getter;

@Getter
public class VariationCategoryForm {
    private String variationName;
    private MeasureUnit measureUnit;
    private String variationDescription;
}
