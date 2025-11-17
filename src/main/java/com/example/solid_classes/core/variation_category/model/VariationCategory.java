package com.example.solid_classes.core.variation_category.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.variation_category.model.enums.MeasureUnit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "variation_categories")
@SuperBuilder
@Getter
@NoArgsConstructor
public class VariationCategory extends AuditableEntity {

    private String variationName;
    private MeasureUnit measureUnit;
    private String variationDescription;

    @OneToMany(mappedBy = "variation_category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariation> productVariations;

    public void addProductVariation(ProductVariation productVariation) {
        this.productVariations.add(productVariation);
    }
}
