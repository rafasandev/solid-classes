package com.example.solid_classes.core.product_variation.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.solid_classes.core.product_variation.model.ProductVariation;

public interface ProductVariationRepository extends MongoRepository<ProductVariation, UUID> {
    List<ProductVariation> findByProductId(UUID productId);
}
