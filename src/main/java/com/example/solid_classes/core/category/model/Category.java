package com.example.solid_classes.core.category.model;

import java.util.ArrayList;
import java.util.List;

import com.example.solid_classes.common.abs.AuditableEntity;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.service.model.Service;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    private final List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Service> services = new ArrayList<>();

    public void addProduct(Product product) {
        this.products.add(product);
        product.setCategory(this);
    }

    public void addService(Service service) {
        this.services.add(service);
        service.setCategory(this);
    }

}
