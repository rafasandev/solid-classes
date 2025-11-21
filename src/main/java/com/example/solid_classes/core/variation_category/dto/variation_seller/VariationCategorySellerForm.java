package com.example.solid_classes.core.variation_category.dto.variation_seller;

import java.util.UUID;

import com.example.solid_classes.core.variation_category.model.enums.MeasureUnit;
import com.example.solid_classes.core.variation_category.model.enums.VariationType;

import lombok.Getter;

@Getter
public class VariationCategorySellerForm {
    private String variationName;
    private MeasureUnit measureUnit;
    private VariationType type;
    private String variationDescription;
    private UUID companyId;
}
