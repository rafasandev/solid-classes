package com.example.market_api.core.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.ports.ProductPort;

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

    public void validateProductNameUniqueness(String productName) {
        if (productPort.existsByProductName(productName)) {
            throw new BusinessRuleException(
                String.format("Já existe um produto cadastrado com o nome '%s'", productName)
            );
        }
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
        if (!product.productHasStock(requestedQuantity)) {
            throw new BusinessRuleException(
                String.format("Estoque insuficiente para '%s'. Disponível: %d, Solicitado: %d",
                    product.getProductName(),
                    product.getStockQuantity(),
                    requestedQuantity)
            );
        }
    }

    public void validateAvailability(Product product) {
        if (!product.productIsAvaiable()) {
            throw new BusinessRuleException(
                String.format("Produto '%s' não está disponível para venda", product.getProductName())
            );
        }
    }

    public void validateProductOwnership(Product product) {
        // Implementação será adicionada quando tivermos acesso ao UserService
        // Por enquanto, esta validação será feita no UseCase
    }
}
