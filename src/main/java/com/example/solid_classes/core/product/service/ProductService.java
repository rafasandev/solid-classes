package com.example.solid_classes.core.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.exception.BusinessRuleException;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.ports.ProductPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPort productPort;

    public Product getById(UUID id) {
        return productPort.getById(id);
    }

    public Product save(Product product) {
        return productPort.save(product);
    }

    public List<Product> findAll() {
        return productPort.findAll();
    }

    public List<Product> findByCompanyId(UUID companyId) {
        return productPort.findByCompanyId(companyId);
    }

    public List<Product> findByCategoryId(UUID categoryId) {
        return productPort.findByCategoryId(categoryId);
    }

    public void validateStock(Product product, int requestedQuantity) {
        if (!product.hasStock(requestedQuantity)) {
            throw new BusinessRuleException(
                String.format("Estoque insuficiente para '%s'. Disponível: %d, Solicitado: %d",
                    product.getProductName(),
                    product.getStockQuantity(),
                    requestedQuantity)
            );
        }
    }

    public void validateAvailability(Product product) {
        if (!product.isAvailable()) {
            throw new BusinessRuleException(
                String.format("Produto '%s' não está disponível para venda", product.getProductName())
            );
        }
    }
}
