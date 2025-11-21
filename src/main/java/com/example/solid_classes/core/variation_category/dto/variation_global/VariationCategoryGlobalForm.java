package com.example.solid_classes.core.variation_category.dto.variation_global;

import com.example.solid_classes.core.variation_category.model.enums.MeasureUnit;
import com.example.solid_classes.core.variation_category.model.enums.VariationType;

import lombok.Getter;

@Getter
public class VariationCategoryGlobalForm {
    private String variationName;
    private MeasureUnit measureUnit;
    private VariationType type;
    private String variationDescription;
}
