package com.example.solid_classes.core.category.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.category.ports.CategoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryPort categoryPort;

    public Category getById(UUID categoryId) {
        return categoryPort.getById(categoryId);
    }

    public Category saveCategory(Category newCategory) {
        return categoryPort.save(newCategory);
    }

}
