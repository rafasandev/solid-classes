package com.example.solid_classes.core.product.service;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.ports.ProductPort;
import com.example.solid_classes.core.product.repository.ProductRepository;

@Component
public class ProductAdapter extends NamedCrudAdapter<Product, ProductRepository> implements ProductPort {

    public ProductAdapter(ProductRepository productRepository) {
        super(productRepository, "Produto");
    }
}
