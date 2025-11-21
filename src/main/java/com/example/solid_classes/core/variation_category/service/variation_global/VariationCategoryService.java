package com.example.solid_classes.core.variation_category.service.variation_global;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;
import com.example.solid_classes.core.variation_category.ports.VariationCategoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationCategoryService {

    private final VariationCategoryPort variationCategoryPort;

    public VariationCategoryEntity getById(java.util.UUID id) {
        return variationCategoryPort.getById(id);
    }

    public VariationCategoryEntity createVariationCategory(VariationCategoryEntity variationCategory) {
        return variationCategoryPort.save(variationCategory);
    }
    
}
