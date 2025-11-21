package com.example.solid_classes.core.variation_category.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.variation_category.dto.VariationCategoryForm;
import com.example.solid_classes.core.variation_category.dto.VariationCategoryResponseDto;
import com.example.solid_classes.core.variation_category.model.VariationCategory;

@Component
public class VariationCategoryMapper {
    
    public VariationCategory toEntity(VariationCategoryForm variationCategoryForm) {
        VariationCategory variationCategory = VariationCategory.builder()
                .variationName(variationCategoryForm.getVariationName())
                .measureUnit(variationCategoryForm.getMeasureUnit())
                .variationDescription(variationCategoryForm.getVariationDescription())
                .build();
        return variationCategory;
    }
    
    public VariationCategoryResponseDto toResponseDto(VariationCategory variationCategory) {
        return VariationCategoryResponseDto.builder()
                .id(variationCategory.getId())
                .variationName(variationCategory.getVariationName())
                .measureUnit(variationCategory.getMeasureUnit().toString())
                .variationDescription(variationCategory.getVariationDescription())
                .build();
    }
}
