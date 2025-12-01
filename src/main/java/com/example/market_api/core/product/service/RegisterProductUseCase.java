package com.example.market_api.core.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.category.service.CategoryService;
import com.example.market_api.core.product.dto.ProductForm;
import com.example.market_api.core.product.dto.ProductResponseDto;
import com.example.market_api.core.product.mapper.ProductMapper;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProductUseCase {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CompanyProfileService companyProfileService;
    private final UserService userService;

    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto registerProduct(ProductForm productForm) {
        User loggedInUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedInUser.getId());
        validateUserIsCompanyOwner(loggedInUser, company);
        
        Category category = categoryService.getById(productForm.getCategoryId());
        
        // Validações de negócio
        companyProfileService.validateIsActive(company);
        companyProfileService.validateBusinessSector(company, BusinessSector.COMMERCE);
        categoryService.validateBusinessSectorCompatibility(category, BusinessSector.COMMERCE);
        productService.validateProductNameUniqueness(productForm.getProductName());

        Product newProduct = productMapper.toEntity(productForm, category, company);
        Product savedProduct = productService.save(newProduct);
        return productMapper.toResponseDto(savedProduct, category, company);
    }

    private void validateUserIsCompanyOwner(User user, CompanyProfile company) {
        if (company == null || !company.getId().equals(user.getId())) {
            throw new com.example.market_api.common.exception.BusinessRuleException(
                    "Usuário logado não é o proprietário da empresa associada ao produto");
        }
    }
}
