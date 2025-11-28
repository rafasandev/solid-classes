package com.example.solid_classes.core.category.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByBusinessSector(BusinessSector businessSector);

    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
