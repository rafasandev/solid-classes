package com.example.market_api.core.variation_category.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.variation_category.dto.VariationCategoryResponseDto;
import com.example.market_api.core.variation_category.dto.variation_global.VariationCategoryGlobalForm;
import com.example.market_api.core.variation_category.dto.variation_seller.VariationCategorySellerForm;
import com.example.market_api.core.variation_category.model.VariationCategoryEntity;
import com.example.market_api.core.variation_category.model.variation_global.VariationCategoryGlobal;
import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;

@Component
public class VariationCategoryMapper {

    public VariationCategoryGlobal toEntity(VariationCategoryGlobalForm variationCategoryGlobalForm) {
        VariationCategoryGlobal variationCategoryGlobal = VariationCategoryGlobal.builder()
                .name(variationCategoryGlobalForm.getVariationName())
                .type(variationCategoryGlobalForm.getType())
                .measureUnit(variationCategoryGlobalForm.getMeasureUnit())
                .description(variationCategoryGlobalForm.getVariationDescription())
                .active(true)
                .build();
                return variationCategoryGlobal;
    }

    public VariationCategorySeller toEntity(VariationCategorySellerForm variationCategorySellerForm,
        CompanyProfile company) {
            VariationCategorySeller variationCategorySeller = VariationCategorySeller.builder()
                .name(variationCategorySellerForm.getVariationName())
                .type(variationCategorySellerForm.getType())
                .measureUnit(variationCategorySellerForm.getMeasureUnit())
                .description(variationCategorySellerForm.getVariationDescription())
                .company(company)
                .active(true)
                .build();

        return variationCategorySeller;
    }

    public VariationCategoryResponseDto toResponseDto(VariationCategoryEntity variationCategory) {
        return VariationCategoryResponseDto.builder()
                .id(variationCategory.getId())
                .variationName(variationCategory.getName())
                .measureUnit(variationCategory.getMeasureUnit().toString())
                .variationDescription(variationCategory.getDescription())
                .build();
    }
}
