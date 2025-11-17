package com.example.solid_classes.core.product_variation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.product_variation.model.ProductVariation;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, UUID> {

}
