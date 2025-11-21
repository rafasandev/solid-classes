package com.example.solid_classes.core.variation_category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.variation_category.dto.VariationCategoryResponseDto;
import com.example.solid_classes.core.variation_category.dto.variation_global.VariationCategoryGlobalForm;
import com.example.solid_classes.core.variation_category.service.RegisterVariationCategoryUseCase;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/variation-category")
@RequiredArgsConstructor
public class VariationCategoryController {

    private final RegisterVariationCategoryUseCase variationCategoryService;

    @PostMapping
    public ResponseEntity<VariationCategoryResponseDto> createVariationCategory(@RequestBody VariationCategoryGlobalForm variationForm) {
        VariationCategoryResponseDto variationCategory = variationCategoryService.registerVariationCategory(variationForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(variationCategory);
    }

}
