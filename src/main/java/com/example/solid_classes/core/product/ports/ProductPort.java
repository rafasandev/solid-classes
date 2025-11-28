package com.example.solid_classes.core.product.ports;

import java.util.List;
import java.util.UUID;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.product.model.Product;

public interface ProductPort extends NamedCrudPort<Product> {
    
    List<Product> findByCompanyId(UUID companyId);
    
    List<Product> findByCategoryId(UUID categoryId);
    
    List<Product> searchByName(String name);
    
    List<Product> findAvailableWithStock();
    
    boolean existsByProductName(String productName);
}
