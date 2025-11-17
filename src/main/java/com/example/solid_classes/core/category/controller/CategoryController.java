package com.example.solid_classes.core.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.category.dto.CategoryForm;
import com.example.solid_classes.core.category.dto.CategoryResponseDto;
import com.example.solid_classes.core.category.service.RegisterCategoryUseCase;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final RegisterCategoryUseCase categoryService;
    
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN_MASTER')")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryForm categoryForm) {
        CategoryResponseDto newCategory = categoryService.registerCategory(categoryForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }
    
}
