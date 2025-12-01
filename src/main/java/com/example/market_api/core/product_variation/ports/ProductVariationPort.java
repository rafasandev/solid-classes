package com.example.market_api.core.product_variation.ports;

import java.util.List;
import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.product_variation.model.ProductVariation;

public interface ProductVariationPort extends NamedCrudPort<ProductVariation>{
    
    List<ProductVariation> findByProductId(UUID productId);
}
