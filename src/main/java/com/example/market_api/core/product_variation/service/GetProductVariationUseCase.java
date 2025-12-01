package com.example.market_api.core.product_variation.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.service.ProductService;
import com.example.market_api.core.product_variation.dto.ProductVariationResponseDto;
import com.example.market_api.core.product_variation.mapper.ProductVariationMapper;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.product_variation.model.enums.VariationCategoryType;
import com.example.market_api.core.variation_category.model.VariationCategoryEntity;
import com.example.market_api.core.variation_category.service.variation_global.VariationCategoryGlobalService;
import com.example.market_api.core.variation_category.service.variation_seller.VariationCategorySellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProductVariationUseCase {

    private final ProductVariationService productVariationService;
    private final ProductService productService;
    private final VariationCategorySellerService variationCategorySellerService;
    private final VariationCategoryGlobalService variationCategoryGlobalService;
    private final ProductVariationMapper productVariationMapper;

    @Transactional(readOnly = true)
    public List<ProductVariationResponseDto> getAllVariations() {
        List<ProductVariation> variations = productVariationService.findAll();
        List<ProductVariationResponseDto> variationsDto = variations.stream()
                .map(variation -> {
                    Product product = productService.getById(variation.getId());
                    VariationCategoryEntity variationCategory = getVariationCategoryByIdAndType(
                            product.getCategoryId(),
                            variation.getVariationCategoryType());
                    return productVariationMapper.toResponseDto(variation, variationCategory, product);
                })
                .toList();

        return variationsDto;
    }

    @Transactional(readOnly = true)
    public ProductVariationResponseDto getVariationById(UUID id) {
        ProductVariation variation = productVariationService.getById(id);
        Product product = productService.getById(variation.getId());
        VariationCategoryEntity variationCategory = getVariationCategoryByIdAndType(
                product.getCategoryId(),
                variation.getVariationCategoryType());
        return productVariationMapper.toResponseDto(variation, variationCategory, product);
    }

    @Transactional(readOnly = true)
    public List<ProductVariationResponseDto> getVariationsByProductId(UUID productId) {
        List<ProductVariation> variations = productVariationService.findByProductId(productId);
        Product product = productService.getById(productId);
        List<ProductVariationResponseDto> variationsDto = variations.stream()
                .map(variation -> {
                    VariationCategoryEntity variationCategory = getVariationCategoryByIdAndType(
                            product.getCategoryId(),
                            variation.getVariationCategoryType());
                    return productVariationMapper.toResponseDto(variation, variationCategory, product);
                })
                .toList();

        return variationsDto;
    }

    private VariationCategoryEntity getVariationCategoryByIdAndType(UUID categoryId, VariationCategoryType type) {
        switch (type) {
            case SELLER:
                return variationCategorySellerService.getById(categoryId);
            case GLOBAL:
                return variationCategoryGlobalService.getById(categoryId);
            default:
                throw new IllegalArgumentException("Tipo de categoria de variação desconhecido: " + type);
        }
    }
}
