package com.example.solid_classes.core.variation_category.service.variation_seller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.variation_category.model.variation_seller.VariationCategorySeller;
import com.example.solid_classes.core.variation_category.ports.VariationCategorySellerPort;
import com.example.solid_classes.core.variation_category.repository.VariationCategorySellerRepository;

@Component
public class VariationCategorySellerAdapter
        extends NamedCrudAdapter<VariationCategorySeller, VariationCategorySellerRepository>
        implements VariationCategorySellerPort {

    public VariationCategorySellerAdapter(VariationCategorySellerRepository variationCategoryRepository) {
        super(variationCategoryRepository, "Categoria de Variação do Vendedor");
    }

    @Override
    public List<VariationCategorySeller> getByCompanyId(UUID companyId) {
        return repository.findByCompanyId(companyId);
    }
 }
