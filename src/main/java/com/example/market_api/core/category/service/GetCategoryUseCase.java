package com.example.market_api.core.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.category.dto.CategoryResponseDto;
import com.example.market_api.core.category.mapper.CategoryMapper;
import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCategoryUseCase {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return categories.stream()
            .map(categoryMapper::toResponseDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(UUID id) {
        Category category = categoryService.getById(id);
        return categoryMapper.toResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoriesByBusinessSector(BusinessSector businessSector) {
        List<Category> categories = categoryService.findByBusinessSector(businessSector);
        return categories.stream()
            .map(categoryMapper::toResponseDto)
            .toList();
    }
}
