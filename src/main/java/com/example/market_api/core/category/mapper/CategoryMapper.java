package com.example.market_api.core.category.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.category.dto.CategoryForm;
import com.example.market_api.core.category.dto.CategoryResponseDto;
import com.example.market_api.core.category.model.Category;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryForm categoryForm) {
        return Category.builder()
                .categoryName(categoryForm.getCategoryName())
                .businessSector(categoryForm.getBusinessSector())
                .build();
    }

    public CategoryResponseDto toResponseDto(Category category) {
        CategoryResponseDto dto = CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .businessSector(category.getBusinessSector())
                .build();
        return dto;
    }
}
