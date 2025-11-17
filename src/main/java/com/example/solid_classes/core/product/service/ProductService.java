package com.example.solid_classes.core.product.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.ports.ProductPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPort productPort;

    public Product createProduct(Product newProduct) {
        return productPort.save(newProduct);
    }
}
