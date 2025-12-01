package com.example.market_api.core.product.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.category.service.CategoryService;
import com.example.market_api.core.product.dto.ProductResponseDto;
import com.example.market_api.core.product.mapper.ProductMapper;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CompanyProfileService companyProfileService;

    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductResponseDto> productsDto = products.stream()
                .map(product -> {
                    Category category = categoryService.getById(product.getCategoryId());
                    CompanyProfile company = companyProfileService.getById(product.getCompanyId());
                    return productMapper.toResponseDto(product, category, company);
                })
                .collect(Collectors.toList());
        return productsDto;
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        Product product = productService.getById(id);
        Category category = categoryService.getById(product.getCategoryId());
        CompanyProfile company = companyProfileService.getById(product.getCompanyId());
        return productMapper.toResponseDto(product, category, company);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCompanyId(UUID companyId) {
        List<Product> products = productService.findByCompanyId(companyId);
        CompanyProfile company = companyProfileService.getById(companyId);
        List<ProductResponseDto> productsDto = products.stream()
                .map(product -> {
                    Category category = categoryService.getById(product.getCategoryId());
                    return productMapper.toResponseDto(product, category, company);
                })
                .collect(Collectors.toList());
        return productsDto;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategoryId(UUID categoryId) {
        List<Product> products = productService.findByCategoryId(categoryId);
        Category category = categoryService.getById(categoryId);
        List<ProductResponseDto> productsDto = products.stream()
                .map(product -> {
                    CompanyProfile company = companyProfileService.getById(product.getCompanyId());
                    return productMapper.toResponseDto(product, category, company);
                })
                .collect(Collectors.toList());
        return productsDto;
    }
}
