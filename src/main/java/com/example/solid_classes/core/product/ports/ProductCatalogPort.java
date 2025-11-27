package com.example.solid_classes.core.product.ports;

import java.util.List;
import java.util.UUID;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.product.model.mongo.ProductDocument;

/**
 * Port para Product no MongoDB.
 */
public interface ProductCatalogPort extends NamedCrudPort<ProductDocument> {
    
    List<ProductDocument> findByCompanyId(UUID companyId);
    
    List<ProductDocument> findByCategoryId(UUID categoryId);
    
    List<ProductDocument> findAvailable();
    
    List<ProductDocument> searchByName(String name);
    
    List<ProductDocument> findAvailableWithStock();
    
    boolean existsByProductName(String productName);
}
