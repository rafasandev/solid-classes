package com.example.market_api.core.variation_category.service.variation_global;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.variation_category.model.variation_global.VariationCategoryGlobal;
import com.example.market_api.core.variation_category.ports.VariationCategoryGlobalPort;
import com.example.market_api.core.variation_category.repository.jpa.VariationCategoryGlobalRepository;

@Component
public class VariationCategoryGlobalAdapter
        extends NamedCrudAdapter<VariationCategoryGlobal, VariationCategoryGlobalRepository>
        implements VariationCategoryGlobalPort {

    public VariationCategoryGlobalAdapter(VariationCategoryGlobalRepository variationCategoryRepository) {
        super(variationCategoryRepository, "Categoria de Variação Global");
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public java.util.Optional<VariationCategoryGlobal> findByName(String name) {
        return repository.findByName(name);
    }
}
