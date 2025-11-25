package com.example.solid_classes.core.product_variation.model;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product_variation.model.enums.VariationValueType;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;

import jakarta.persistence.Column;
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

    @Column(nullable = false, length = 100)
    private String variationValue;
    
    @Column(nullable = false)
    private VariationValueType valueType;
    
    @Column(nullable = false)
    private double variationAdditionalPrice;

    @Setter
    @Column(nullable = false)
    private int stockQuantity;
    
    @Column(nullable = false)
    private boolean available;

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

    public void decreaseStock(int quantity) {
        if (quantity > 0 && this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            if (this.stockQuantity == 0) {
                this.available = false;
            }
        }
    }

    public void increaseStock(int quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
            this.available = true;
        }
    }

    public boolean hasStock(int quantity) {
        return this.available && this.stockQuantity >= quantity;
    }

    public void toggleAvailability() {
        this.available = !this.available;
    }

    public double calculateTotalPrice(Product product) {
        return product.getPriceBase() + this.variationAdditionalPrice;
    }
}
