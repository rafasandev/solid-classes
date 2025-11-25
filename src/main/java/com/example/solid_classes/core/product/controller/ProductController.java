package com.example.solid_classes.core.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.solid_classes.core.product.dto.ProductForm;
import com.example.solid_classes.core.product.dto.ProductResponseDto;
import com.example.solid_classes.core.product.service.RegisterProductUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final RegisterProductUseCase productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductForm productForm) {
        ProductResponseDto productResponse = productService.registerProduct(productForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

}
