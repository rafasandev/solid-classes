package com.example.solid_classes.core.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.category.dto.CategoryForm;
import com.example.solid_classes.core.category.interfaces.CategoryPort;
import com.example.solid_classes.core.category.model.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryPort categoryPort;

    @Transactional
    public Category createCategory(CategoryForm categoryForm) {
        Category newCategory = Category.create(categoryForm.getCategoryName());

        return categoryPort.save(newCategory);
    }
}
