package com.example.market_api.core.product_variation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.service.ProductService;
import com.example.market_api.core.product_variation.dto.ProductVariationForm;
import com.example.market_api.core.product_variation.dto.ProductVariationResponseDto;
import com.example.market_api.core.product_variation.mapper.ProductVariationMapper;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;
import com.example.market_api.core.variation_category.model.VariationCategoryEntity;
import com.example.market_api.core.variation_category.service.variation_global.VariationCategoryGlobalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProductVariationUseCase {

    private final ProductVariationService productVariationService;
    private final ProductService productService;
    private final VariationCategoryGlobalService variationCategoryGlobalService;
    private final ProductVariationMapper productVariationMapper;
    private final UserService userService;
    private final CompanyProfileService companyProfileService;

    @Transactional
    public ProductVariationResponseDto registerProductVariation(ProductVariationForm variationForm) {
        // Busca o produto
        Product product = productService.getById(variationForm.getProductId());
        
        // Valida ownership: usuário logado deve ser dono da empresa que possui o produto
        User loggedInUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedInUser.getId());
        
        if (!product.getCompanyId().equals(company.getId())) {
            throw new BusinessRuleException(
                "Você não tem permissão para adicionar variações a este produto. O produto pertence a outra empresa"
            );
        }

        // Valida categoria de variação
        VariationCategoryEntity category = variationCategoryGlobalService.getById(variationForm.getVariationCategoryId());
        variationCategoryGlobalService.verifyCategoryIsActive(category.getId());

        ProductVariation newVariation = productVariationMapper.toEntity(variationForm, category);
        ProductVariation savedVariation = productVariationService.save(newVariation);
        product.addVariation(savedVariation);
        // Persist product so the embedded variation list and aggregate stock are updated
        productService.save(product);

        return productVariationMapper.toResponseDto(savedVariation, category, product);
    }
}
