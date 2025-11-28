package com.example.solid_classes.core.category.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.exception.BusinessRuleException;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.category.ports.CategoryPort;
import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryPort categoryPort;

    public Category getById(UUID id) {
        return categoryPort.getById(id);
    }

    public Category save(Category category) {
        Optional<Category> existing = categoryPort.findByCategoryName(category.getCategoryName());
        if (existing.isPresent() && (category.getId() == null || !existing.get().getId().equals(category.getId()))) {
            throw new com.example.solid_classes.common.exception.BusinessRuleException(
                    String.format("Categoria '%s' já existe", category.getCategoryName()));
        }

        return categoryPort.save(category);
    }

    public List<Category> findAll() {
        return categoryPort.findAll();
    }

    public List<Category> findByBusinessSector(BusinessSector businessSector) {
        return categoryPort.findByBusinessSector(businessSector);
    }

    public void validateBusinessSectorCompatibility(Category category, BusinessSector expectedSector) {
        if (category.getBusinessSector() != expectedSector) {
            throw new BusinessRuleException(
                    String.format("Categoria '%s' não é compatível com %s. Setor esperado: %s, Setor da categoria: %s",
                            category.getCategoryName(),
                            expectedSector == BusinessSector.COMMERCE ? "produtos" : "serviços",
                            expectedSector,
                            category.getBusinessSector()));
        }
    }
}
