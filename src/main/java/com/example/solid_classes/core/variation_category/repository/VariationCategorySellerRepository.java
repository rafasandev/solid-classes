package com.example.solid_classes.core.variation_category.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.variation_category.model.variation_seller.VariationCategorySeller;

public interface VariationCategorySellerRepository extends JpaRepository<VariationCategorySeller, UUID> {

    List<VariationCategorySeller> findByCompanyId(UUID companyId);
}
