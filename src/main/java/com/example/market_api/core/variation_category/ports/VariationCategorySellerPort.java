package com.example.market_api.core.variation_category.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;

public interface VariationCategorySellerPort extends NamedCrudPort<VariationCategorySeller> {

    List<VariationCategorySeller> getByCompanyId(UUID companyId);

    boolean existsByNameAndCompanyId(String name, UUID companyId);

    Optional<VariationCategorySeller> findByNameAndCompanyId(String name, UUID companyId);
}
