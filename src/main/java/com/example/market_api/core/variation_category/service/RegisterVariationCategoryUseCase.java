package com.example.market_api.core.variation_category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.variation_category.dto.VariationCategoryResponseDto;
import com.example.market_api.core.variation_category.dto.variation_global.VariationCategoryGlobalForm;
import com.example.market_api.core.variation_category.dto.variation_seller.VariationCategorySellerForm;
import com.example.market_api.core.variation_category.mapper.VariationCategoryMapper;
import com.example.market_api.core.variation_category.model.variation_global.VariationCategoryGlobal;
import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;
import com.example.market_api.core.variation_category.service.variation_global.VariationCategoryGlobalService;
import com.example.market_api.core.variation_category.service.variation_seller.VariationCategorySellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterVariationCategoryUseCase {
    
    private final VariationCategoryGlobalService variationCategoryGlobalService;
    private final VariationCategorySellerService variationCategorySellerService;
    private final VariationCategoryMapper variationCategoryMapper;
    private final CompanyProfileService companyProfileService;

    @Transactional
    public VariationCategoryResponseDto registerVariationCategory(VariationCategoryGlobalForm variationCategoryForm) {
        VariationCategoryGlobal variationCategory = variationCategoryMapper.toEntity(variationCategoryForm);
        VariationCategoryGlobal savedVariationCategory = variationCategoryGlobalService.save(variationCategory);
        VariationCategoryResponseDto responseDto = variationCategoryMapper.toResponseDto(savedVariationCategory);
        return responseDto;
    }

    @Transactional
    public VariationCategoryResponseDto registerVariationCategory(VariationCategorySellerForm variationCategoryForm) {
        CompanyProfile company = companyProfileService.getById(variationCategoryForm.getCompanyId());

        if(!company.isActive())
            throw new BusinessRuleException("Empresa inativa. Operação falhou");

        VariationCategorySeller variationCategory = variationCategoryMapper.toEntity(variationCategoryForm, company);
        VariationCategorySeller savedVariationCategory = variationCategorySellerService.save(variationCategory);
        VariationCategoryResponseDto responseDto = variationCategoryMapper.toResponseDto(savedVariationCategory);
        return responseDto;
    }
}
