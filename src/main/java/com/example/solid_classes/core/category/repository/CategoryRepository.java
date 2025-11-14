package com.example.solid_classes.core.category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
