package com.example.solid_classes.core.product_variation.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.product_variation.ports.ProductVariationPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariationService {

    private final ProductVariationPort productVariationPort;

    public ProductVariation createProductVariation(ProductVariation newProductVariation) {
        return productVariationPort.save(newProductVariation);
    }
    
}
