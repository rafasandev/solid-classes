package com.example.solid_classes.core.product_variation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.product_variation.dto.ProductVariationForm;
import com.example.solid_classes.core.product_variation.dto.ProductVariationResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/product-variation")
@RequiredArgsConstructor
public class ProductVariationController {
    

    @PostMapping
    public ResponseEntity<ProductVariationResponseDto> createProductVariation(@RequestBody ProductVariationForm variationForm) {

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
        
}
