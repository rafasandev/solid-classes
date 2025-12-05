package com.example.market_api.core.category.model;

import java.util.Set;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.variation_category.model.variation_global.VariationCategoryGlobal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "categories")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Category extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Column(nullable = false)
    private BusinessSector businessSector;

    @ManyToMany(mappedBy = "categories")
    private Set<VariationCategoryGlobal> variationCategories;

    public void addVariationCategory(VariationCategoryGlobal variationCategory) {
        if (variationCategory != null && !this.variationCategories.contains(variationCategory)){
            this.variationCategories.add(variationCategory);
        }
    }

    public void removeVariationCategory(VariationCategoryGlobal variationCategory) {
        if (variationCategory != null && this.variationCategories.contains(variationCategory)){
            this.variationCategories.remove(variationCategory);
        }
    }
}
