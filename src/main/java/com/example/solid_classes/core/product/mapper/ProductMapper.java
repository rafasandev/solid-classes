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
                .category(category)
                .company(company)
                .build();
        return product;
    }

    public ProductResponseDto toResponseDto(Product product) {
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getProductName())
                .description(product.getDescription())
                .categoryName(product.getCategory().getCategoryName())
                .companyName(product.getCompany().getCompanyName())
                .build();
        return responseDto;
    }
}
