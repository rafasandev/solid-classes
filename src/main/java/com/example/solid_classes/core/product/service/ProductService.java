package com.example.solid_classes.core.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.category.interfaces.CategoryPort;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.product.dto.ProductForm;
import com.example.solid_classes.core.product.interfaces.ProductPort;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.profile.interfaces.CompanyProfilePort;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPort productPort;
    private final CategoryPort categoryPort;
    private final CompanyProfilePort companyPort;

    @Transactional
    public Product createProduct(ProductForm productForm) {
        Category category = categoryPort.getById(productForm.getCategoryId());
        CompanyProfile company = companyPort.getById(productForm.getCompanyId());

        Product newProduct = Product.create(
                productForm.getProductName(),
                category,
                company);

        return productPort.save(newProduct);
    }
}
