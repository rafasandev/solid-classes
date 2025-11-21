package com.example.solid_classes.core.variation_category.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.variation_category.model.VariationCategory;
import com.example.solid_classes.core.variation_category.ports.VariationCategoryPort;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationCategoryService {

    private final VariationCategoryPort variationCategoryPort;

    public VariationCategory getById(java.util.UUID id) {
        return variationCategoryPort.getById(id);
    }

    public VariationCategory createVariationCategory(VariationCategory variationCategory) {
        return variationCategoryPort.save(variationCategory);
    }
    
}
