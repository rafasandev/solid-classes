package com.example.market_api.core.category.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.category.dto.CategoryForm;
import com.example.market_api.core.category.dto.CategoryResponseDto;
import com.example.market_api.core.category.service.GetCategoryUseCase;
import com.example.market_api.core.category.service.RegisterCategoryUseCase;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final RegisterCategoryUseCase registerCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryForm categoryForm) {
        CategoryResponseDto newCategory = registerCategoryUseCase.registerCategory(categoryForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(getCategoryUseCase.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(getCategoryUseCase.getCategoryById(id));
    }

    @GetMapping("/sector/{businessSector}")
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesByBusinessSector(
            @PathVariable BusinessSector businessSector) {
        return ResponseEntity.ok(getCategoryUseCase.getCategoriesByBusinessSector(businessSector));
    }
}
