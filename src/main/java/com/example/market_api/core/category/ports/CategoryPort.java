package com.example.market_api.core.category.ports;

import java.util.List;
import java.util.Optional;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;

public interface CategoryPort extends NamedCrudPort<Category>{
    List<Category> findByBusinessSector(BusinessSector businessSector);

    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
