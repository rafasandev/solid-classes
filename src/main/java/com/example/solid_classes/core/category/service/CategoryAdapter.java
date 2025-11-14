package com.example.solid_classes.core.category.service;


import org.springframework.stereotype.Service;

import com.example.solid_classes.common.classes.NamedCrudAdapter;
import com.example.solid_classes.core.category.interfaces.CategoryPort;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.category.repository.CategoryRepository;

@Service
public class CategoryAdapter extends NamedCrudAdapter<Category, CategoryRepository> implements CategoryPort{
    
    public CategoryAdapter(CategoryRepository categoryRepository) {
        super(categoryRepository, "Categoria");
    }
    
}
