package com.example.solid_classes.core.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.category.dto.CategoryForm;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN_MASTER')")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryForm categoryForm) {
        Category newCategory = categoryService.createCategory(categoryForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }
    
}
