package com.example.solid_classes.core.category.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;
import com.example.solid_classes.core.variation_category.model.variation_global.VariationCategoryGlobal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceOffering> services;

    @ManyToMany(mappedBy = "categories")
    private List<VariationCategoryGlobal> variationCategories;

    public void addProduct(Product product) {
        if (product != null && this.products != null && !this.products.contains(product))
            this.products.add(product);
    }

    public void removeProduct(Product product) {
        if (product != null && this.products != null && this.products.contains(product))
            this.products.remove(product);
    }

    public void addService(ServiceOffering service) {
        if (service != null && this.services != null && !this.services.contains(service))
            this.services.add(service);
    }

    public void removeService(ServiceOffering service) {
        if (service != null && this.services != null && this.services.contains(service))
            this.services.remove(service);
    }



}
