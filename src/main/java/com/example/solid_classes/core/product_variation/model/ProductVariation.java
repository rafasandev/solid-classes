package com.example.solid_classes.core.product_variation.model;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product_variation.model.abs.VariationValueType;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_variations")
@Getter
@SuperBuilder
@NoArgsConstructor
public class ProductVariation extends AuditableEntity {

    private String variationValue;
    private VariationValueType valueType;
    private double variationAdditionalPrice;

    @Setter
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_category_id", nullable = false)
    private VariationCategoryEntity variationCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public void setVariationCategory(VariationCategoryEntity variationCategory) {
        this.variationCategory = variationCategory;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
