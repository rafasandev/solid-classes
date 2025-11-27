package com.example.solid_classes.core.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedMongoAdapter;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.ports.ProductPort;
import com.example.solid_classes.core.product.repository.ProductRepository;

@Component
public class ProductAdapter extends NamedMongoAdapter<Product, ProductRepository> implements ProductPort {

    public ProductAdapter(ProductRepository repository) {
        super(repository, "Produto");
    }

    public List<Product> findByCompanyId(UUID companyId) {
        return repository.findByCompanyId(companyId);
    }

    public List<Product> findByCategoryId(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public List<Product> findAvailable() {
        return repository.findByAvailableTrue();
    }

    public List<Product> searchByName(String name) {
        return repository.searchByName(name);
    }

    public List<Product> findAvailableWithStock() {
        return repository.findAvailableWithStock();
    }

    public boolean existsByProductName(String productName) {
        return repository.existsByProductName(productName);
    }
}
