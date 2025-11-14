package com.example.solid_classes.core.category.model;

import java.util.ArrayList;
import java.util.List;

import com.example.solid_classes.common.classes.AuditableEntity;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class Category extends AuditableEntity {

    private Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<ServiceOffering> services = new ArrayList<>();

    public static Category create(String categoryName) {
        return new Category(categoryName);
    }

    public void addProduct(Product product) {
        if (product != null && !this.products.contains(product))
            this.products.add(product);
    }

    public void removeProduct(Product product) {
        if (product != null && this.products.contains(product))
            this.products.remove(product);
    }

    public void addService(ServiceOffering service) {
        if (service != null && !this.services.contains(service))
            this.services.add(service);
    }

    public void removeService(ServiceOffering service) {
        if (service != null && this.services.contains(service))
            this.services.remove(service);
    }

}
