package com.example.market_api.core.variation_category.service.variation_seller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;
import com.example.market_api.core.variation_category.ports.VariationCategorySellerPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationCategorySellerService {
    
    private final VariationCategorySellerPort variationCategorySellerPort;

    public VariationCategorySeller getById(UUID id) {
        return variationCategorySellerPort.getById(id);
    }

    public VariationCategorySeller save(VariationCategorySeller variationCategory) {
        // Prevent duplicate seller-scoped variation names
        if (variationCategory.getName() != null && variationCategory.getCompany() != null) {
            UUID companyId = variationCategory.getCompany().getId();
            if (variationCategorySellerPort.existsByNameAndCompanyId(variationCategory.getName(), companyId)) {
                throw new BusinessRuleException(
                    String.format("Variação '%s' já existe para a empresa %s", variationCategory.getName(), companyId)
                );
            }
        }

        return variationCategorySellerPort.save(variationCategory);
    }

    public List<VariationCategorySeller> findAll() {
        return variationCategorySellerPort.findAll();
    }
}
