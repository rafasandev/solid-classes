package com.example.solid_classes.core.product.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.product.dto.ProductForm;
import com.example.solid_classes.core.product.dto.ProductResponseDto;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;

@Component
public class ProductMapper {

    public Product toEntity(ProductForm productForm, Category category, CompanyProfile company) {
        Product product = Product.builder()
                .productName(productForm.getProductName())
                .description(productForm.getProductDescription())
                .basePrice(productForm.getPriceBase())
                .stockQuantity(productForm.getStockQuantity())
                .categoryId(category.getId())
                .companyId(company.getId())
                .build();
        return product;
    }

    public ProductResponseDto toResponseDto(Product product, Category category, CompanyProfile company) {
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getProductName())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .stockQuantity(product.getStockQuantity())
                .categoryName(category.getCategoryName())
                .companyName(company.getCompanyName())
                .build();
        return responseDto;
    }

}
