package com.example.market_api.core.product_variation.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.product_variation.ports.ProductVariationPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariationService {

    private final ProductVariationPort productVariationPort;

    public ProductVariation getById(UUID id) {
        return productVariationPort.getById(id);
    }

    public ProductVariation save(ProductVariation variation) {
        return productVariationPort.save(variation);
    }

    public List<ProductVariation> findAll() {
        return productVariationPort.findAll();
    }

    public List<ProductVariation> findByProductId(UUID productId) {
        return productVariationPort.findByProductId(productId);
    }

    public void validateStock(ProductVariation variation, int requestedQuantity) {
        if (!variation.hasStock(requestedQuantity)) {
            throw new BusinessRuleException(
                String.format("Estoque insuficiente para variação '%s'. Disponível: %d, Solicitado: %d",
                    variation.getVariationValue(),
                    variation.getStockQuantity(),
                    requestedQuantity)
            );
        }
    }

    public void validateAvailability(ProductVariation variation) {
        if (!variation.isAvailable()) {
            throw new BusinessRuleException(
                String.format("Variação '%s' não está disponível", variation.getVariationValue())
            );
        }
    }
}
