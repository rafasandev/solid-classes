package com.example.solid_classes.core.variation_category.service.variation_global;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.exception.BusinessRuleException;
import com.example.solid_classes.core.variation_category.model.variation_global.VariationCategoryGlobal;
import com.example.solid_classes.core.variation_category.ports.VariationCategoryGlobalPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationCategoryGlobalService {

    private final VariationCategoryGlobalPort variationCategoryGlobalPort;

    public VariationCategoryGlobal getById(UUID id) {
        return variationCategoryGlobalPort.getById(id);
    }

    public VariationCategoryGlobal save(VariationCategoryGlobal variationCategory) {
        if (variationCategory.getName() != null && variationCategoryGlobalPort.existsByName(variationCategory.getName())) {
            throw new BusinessRuleException(
                String.format("Variação global '%s' já existe", variationCategory.getName())
            );
        }

        return variationCategoryGlobalPort.save(variationCategory);
    }

    public List<VariationCategoryGlobal> findAll() {
        return variationCategoryGlobalPort.findAll();
    }

    public void verifyCategoryIsActive(UUID id) {
        VariationCategoryGlobal category = getById(id);
        if (!category.isActive()) {
            throw new BusinessRuleException("Categoria de variação global inativa. Operação falhou");
        }
    }
}
