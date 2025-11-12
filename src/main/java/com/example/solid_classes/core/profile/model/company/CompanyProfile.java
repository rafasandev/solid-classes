package com.example.solid_classes.core.profile.model.company;

import java.util.ArrayList;
import java.util.List;

import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.profile.model.ProfileEntity;
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
@Table(name = "company_profiles")
@Getter
@SuperBuilder
@NoArgsConstructor
public class CompanyProfile extends ProfileEntity {

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Service> services = new ArrayList<>();

    public void addProduct(Product product) {
        this.products.add(product);
        product.setCompany(this);
    }

    public void addService(Service service) {
        this.services.add(service);
        service.setCompany(this);
    }
}
