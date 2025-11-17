package com.example.solid_classes.core.product_variation.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.variation_category.model.VariationCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_variations")
@Getter
@SuperBuilder
@NoArgsConstructor
public class ProductVariation extends AuditableEntity {

    private String variationValue;
    private Double variationPrice;

    @ManyToOne
    @JoinColumn(name = "variation_category_id", nullable = false)
    private VariationCategory variationCategory;

    @ManyToMany(mappedBy = "variationCategories")
    private List<Product> products;

    public void setVariationCategory(VariationCategory variationCategory) {
        this.variationCategory = variationCategory;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
