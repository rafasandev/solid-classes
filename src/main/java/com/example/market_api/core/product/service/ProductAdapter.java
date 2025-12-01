package com.example.market_api.core.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedMongoAdapter;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.ports.ProductPort;
import com.example.market_api.core.product.repository.mongo.ProductRepository;

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
