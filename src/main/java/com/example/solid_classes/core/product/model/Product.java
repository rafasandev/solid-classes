package com.example.solid_classes.core.product.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "products")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Product extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String productName;

    private String description;
    private double priceBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyProfile company;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductVariation> productVariations;

    public void setCategory(Category category) {
        this.category.removeProduct(this);
        if (this.category != null)

        this.category = category;

        if (category != null)
            category.addProduct(this);
    }

    public void setCompany(CompanyProfile company) {
        if (this.company != null)
            this.company.removeProduct(this);

        this.company = company;

        if (company != null)
            company.addProduct(this);
    }

    public void addProductVariation(ProductVariation productVariation) {
        if (productVariation != null && this.productVariations != null)
            this.productVariations.add(productVariation);
    }

    public void removeProductVariation(ProductVariation productVariation) {
        if (productVariation != null && this.productVariations != null)
            this.productVariations.remove(productVariation);
    }

}
