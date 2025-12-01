package com.example.market_api.core.category.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.category.ports.CategoryPort;
import com.example.market_api.core.category.repository.jpa.CategoryRepository;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;

@Component
public class CategoryAdapter extends NamedCrudAdapter<Category, CategoryRepository> implements CategoryPort {

    public CategoryAdapter(CategoryRepository categoryRepository) {
        super(categoryRepository, "Categoria");
    }

    @Override
    public List<Category> findByBusinessSector(BusinessSector businessSector) {
        return repository.findByBusinessSector(businessSector);
    }

    @Override
    public java.util.Optional<Category> findByCategoryName(String categoryName) {
        return repository.findByCategoryName(categoryName);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return repository.existsByCategoryName(categoryName);
    }
}
