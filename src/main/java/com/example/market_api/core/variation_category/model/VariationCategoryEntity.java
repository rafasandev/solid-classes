package com.example.market_api.core.variation_category.model;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.variation_category.model.enums.MeasureUnit;
import com.example.market_api.core.variation_category.model.enums.VariationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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

    @Column(nullable = false)
    protected MeasureUnit measureUnit;

    @Column(nullable = false)
    protected String description;

    @Column(nullable = false)
    protected boolean active;

    // Product variations are stored inside MongoDB `Product` documents as embedded objects.
    // Do NOT model ProductVariation as a JPA relationship here to avoid mixing persistence technologies.
}
