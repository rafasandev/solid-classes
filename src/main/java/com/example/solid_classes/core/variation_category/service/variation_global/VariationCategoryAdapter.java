package com.example.solid_classes.core.variation_category.service.variation_global;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;
import com.example.solid_classes.core.variation_category.ports.VariationCategoryPort;
import com.example.solid_classes.core.variation_category.repository.VariationCategorySellerRepository;

@Service
public class VariationCategoryAdapter extends NamedCrudAdapter<VariationCategoryEntity, VariationCategorySellerRepository> implements VariationCategoryPort {
    
    public VariationCategoryAdapter(VariationCategorySellerRepository variationCategoryRepository) {
        super(variationCategoryRepository, "Categoria de Variação");
    }
}
