package com.example.solid_classes.core.variation_category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.variation_category.model.VariationCategory;

public interface VariationCategoryRepository extends JpaRepository<VariationCategory, UUID> {

}
