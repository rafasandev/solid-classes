package com.example.solid_classes.core.variation_category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;

public interface VariationCategorySellerRepository extends JpaRepository<VariationCategoryEntity, UUID> {

}
