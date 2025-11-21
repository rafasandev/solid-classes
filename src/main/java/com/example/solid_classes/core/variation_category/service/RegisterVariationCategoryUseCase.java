package com.example.solid_classes.core.variation_category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.variation_category.dto.VariationCategoryForm;
import com.example.solid_classes.core.variation_category.dto.VariationCategoryResponseDto;
import com.example.solid_classes.core.variation_category.mapper.VariationCategoryMapper;
import com.example.solid_classes.core.variation_category.model.VariationCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterVariationCategoryUseCase {
    
    private final VariationCategoryService variationCategoryService;
    private final VariationCategoryMapper variationCategoryMapper;

    @Transactional
    public VariationCategoryResponseDto registerVariationCategory(VariationCategoryForm variationCategoryForm) {
        VariationCategory variationCategory = variationCategoryMapper.toEntity(variationCategoryForm);
        VariationCategory savedVariationCategory = variationCategoryService.createVariationCategory(variationCategory);
        VariationCategoryResponseDto responseDto = variationCategoryMapper.toResponseDto(savedVariationCategory);
        return responseDto;
    }
}
