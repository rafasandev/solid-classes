package com.example.solid_classes.core.variation_category.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.variation_category.model.variation_global.VariationCategoryGlobal;

public interface VariationCategoryGlobalRepository extends JpaRepository<VariationCategoryGlobal, UUID> {
	boolean existsByName(String name);
	Optional<VariationCategoryGlobal> findByName(String name);
}
