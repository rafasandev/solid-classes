package com.example.solid_classes.core.variation_category.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.variation_category.model.enums.MeasureUnit;
import com.example.solid_classes.core.variation_category.model.enums.VariationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "variation_categories")
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class VariationCategoryEntity extends AuditableEntity {

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected VariationType type;

    @Column(nullable = false, unique = true)
    protected MeasureUnit measureUnit;

    @Column(nullable = false)
    protected String description;

    @Column(nullable = false)
    protected boolean active;

    @OneToMany(mappedBy = "variationCategory", fetch = FetchType.LAZY)
    protected List<ProductVariation> productVariations;
}
