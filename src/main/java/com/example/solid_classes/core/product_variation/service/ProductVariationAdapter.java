package com.example.solid_classes.core.product_variation.service;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedMongoAdapter;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.product_variation.ports.ProductVariationPort;
import com.example.solid_classes.core.product_variation.repository.ProductVariationRepository;

@Component
public class ProductVariationAdapter extends NamedMongoAdapter<ProductVariation, ProductVariationRepository> implements ProductVariationPort {

    public ProductVariationAdapter(ProductVariationRepository repository) {
        super(repository, "Variação de Produto");
    }

    @Override
    public java.util.List<ProductVariation> findByProductId(java.util.UUID productId) {
        return repository.findByProductId(productId);
    }
}
