package com.example.market_api.core.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.category.dto.CategoryForm;
import com.example.market_api.core.category.dto.CategoryResponseDto;
import com.example.market_api.core.category.mapper.CategoryMapper;
import com.example.market_api.core.category.model.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterCategoryUseCase {
    
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDto registerCategory(CategoryForm categoryForm) {
        Category newCategory = categoryMapper.toEntity(categoryForm);
        Category savedCategory = categoryService.save(newCategory);
        
        return categoryMapper.toResponseDto(savedCategory);
    }
}
