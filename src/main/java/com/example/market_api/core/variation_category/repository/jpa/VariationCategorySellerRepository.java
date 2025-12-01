package com.example.market_api.core.variation_category.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;

public interface VariationCategorySellerRepository extends JpaRepository<VariationCategorySeller, UUID> {

    List<VariationCategorySeller> findByCompanyId(UUID companyId);

    boolean existsByNameAndCompanyId(String name, UUID companyId);

    Optional<VariationCategorySeller> findByNameAndCompanyId(String name, UUID companyId);
}
