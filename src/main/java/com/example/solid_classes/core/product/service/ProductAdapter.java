package com.example.solid_classes.core.product.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.classes.NamedCrudAdapter;
import com.example.solid_classes.core.product.interfaces.ProductPort;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.repository.ProductRepository;

@Service
public class ProductAdapter extends NamedCrudAdapter<Product, ProductRepository> implements ProductPort {

    public ProductAdapter(ProductRepository productRepository) {
        super(productRepository, "Produto");
    }
}
