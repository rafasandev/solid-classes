package com.example.solid_classes.core.product.adapter;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedMongoAdapter;
import com.example.solid_classes.core.product.model.mongo.ProductDocument;
import com.example.solid_classes.core.product.ports.ProductCatalogPort;
import com.example.solid_classes.core.product.repository.mongo.ProductMongoRepository;

@Component
public class ProductMongoAdapter extends NamedMongoAdapter<ProductDocument, ProductMongoRepository> implements ProductCatalogPort {

    public ProductMongoAdapter(ProductMongoRepository repository) {
        super(repository, "Produto");
    }

    public List<ProductDocument> findByCompanyId(UUID companyId) {
        return repository.findByCompanyId(companyId);
    }

    public List<ProductDocument> findByCategoryId(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public List<ProductDocument> findAvailable() {
        return repository.findByAvailableTrue();
    }

    public List<ProductDocument> searchByName(String name) {
        return repository.searchByName(name);
    }

    public List<ProductDocument> findAvailableWithStock() {
        return repository.findAvailableWithStock();
    }

    public boolean existsByProductName(String productName) {
        return repository.existsByProductName(productName);
    }
}
