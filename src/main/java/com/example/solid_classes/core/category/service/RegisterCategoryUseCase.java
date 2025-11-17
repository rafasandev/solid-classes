package com.example.solid_classes.core.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.category.dto.CategoryForm;
import com.example.solid_classes.core.category.dto.CategoryResponseDto;
import com.example.solid_classes.core.category.mapper.CategoryMapper;
import com.example.solid_classes.core.category.model.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterCategoryUseCase {
    
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDto registerCategory(CategoryForm categoryForm) {
        Category newCategory = categoryMapper.toEntity(categoryForm);
        Category savedCategory = categoryService.saveCategory(newCategory);
        CategoryResponseDto responseDto = categoryMapper.toResponseDto(savedCategory);
        return responseDto;
    }
}
