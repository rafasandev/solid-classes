package com.example.solid_classes.core.variation_category.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.variation_category.model.VariationCategory;
import com.example.solid_classes.core.variation_category.ports.VariationCategoryPort;
import com.example.solid_classes.core.variation_category.repository.VariationCategoryRepository;

@Service
public class VariationCategoryAdapter extends NamedCrudAdapter<VariationCategory, VariationCategoryRepository> implements VariationCategoryPort {
    
    public VariationCategoryAdapter(VariationCategoryRepository variationCategoryRepository) {
        super(variationCategoryRepository, "Categoria de Variação");
    }
}
