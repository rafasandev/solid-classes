package com.example.market_api.core.product_variation.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.service.ProductService;
import com.example.market_api.core.product_variation.dto.ProductVariationResponseDto;
import com.example.market_api.core.product_variation.dto.ProductVariationStockUpdateForm;
import com.example.market_api.core.product_variation.mapper.ProductVariationMapper;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.product_variation.model.enums.VariationCategoryType;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;
import com.example.market_api.core.variation_category.model.VariationCategoryEntity;
import com.example.market_api.core.variation_category.service.variation_global.VariationCategoryGlobalService;
import com.example.market_api.core.variation_category.service.variation_seller.VariationCategorySellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateProductVariationStockUseCase {

    private final ProductVariationService productVariationService;
    private final ProductService productService;
    private final ProductVariationMapper productVariationMapper;
    private final VariationCategoryGlobalService variationCategoryGlobalService;
    private final VariationCategorySellerService variationCategorySellerService;
    private final UserService userService;
    private final CompanyProfileService companyProfileService;

    @Transactional
    public ProductVariationResponseDto updateVariationStock(
            UUID variationId,
            ProductVariationStockUpdateForm stockUpdateForm) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        ProductVariation variation = productVariationService.getById(variationId);
        Product product = productService.getById(variation.getProductId());
        validateOwnership(product, company);

        int newQuantity = stockUpdateForm.getStockQuantity();
        variation.updateStockQuantity(newQuantity);
        ProductVariation savedVariation = productVariationService.save(variation);
        productService.save(product);

        VariationCategoryEntity variationCategory = resolveVariationCategory(savedVariation);
        return productVariationMapper.toResponseDto(savedVariation, variationCategory, product);
    }

    private void validateOwnership(Product product, CompanyProfile company) {
        if (!product.getCompanyId().equals(company.getId())) {
            throw new BusinessRuleException("Produto não pertence à empresa autenticada");
        }
    }

    private VariationCategoryEntity resolveVariationCategory(ProductVariation variation) {
        VariationCategoryType type = variation.getVariationCategoryType();
        UUID categoryId = variation.getVariationCategoryId();

        if (type == VariationCategoryType.SELLER) {
            return variationCategorySellerService.getById(categoryId);
        }
        return variationCategoryGlobalService.getById(categoryId);
    }
}
