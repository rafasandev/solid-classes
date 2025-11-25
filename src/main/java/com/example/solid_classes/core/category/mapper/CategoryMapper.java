package com.example.solid_classes.core.category.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.category.dto.CategoryForm;
import com.example.solid_classes.core.category.dto.CategoryResponseDto;
import com.example.solid_classes.core.category.model.Category;

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
                .build();
        return dto;
    }
}
