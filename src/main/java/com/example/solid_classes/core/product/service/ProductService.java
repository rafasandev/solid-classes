package com.example.solid_classes.core.product.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.ports.ProductPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPort productPort;

    public Product getById(UUID id) {
        return getById(id);
    }

    public Product createProduct(Product newProduct) {
        return productPort.save(newProduct);
    }
}
