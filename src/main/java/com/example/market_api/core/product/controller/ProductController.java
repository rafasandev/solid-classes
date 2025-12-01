package com.example.market_api.core.product.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.product.dto.ProductForm;
import com.example.market_api.core.product.dto.ProductResponseDto;
import com.example.market_api.core.product.service.GetProductUseCase;
import com.example.market_api.core.product.service.RegisterProductUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final GetProductUseCase getProductUseCase;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductForm productForm) {
        ProductResponseDto newProduct = registerProductUseCase.registerProduct(productForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(getProductUseCase.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(getProductUseCase.getProductById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(getProductUseCase.getProductsByCompanyId(companyId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(getProductUseCase.getProductsByCategoryId(categoryId));
    }
}
