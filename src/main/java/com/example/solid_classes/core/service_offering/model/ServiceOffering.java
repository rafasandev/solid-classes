package com.example.solid_classes.core.service_offering.model;

import com.example.solid_classes.common.classes.AuditableEntity;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "services")
@Getter
@NoArgsConstructor
public class ServiceOffering extends AuditableEntity {

    private ServiceOffering(String serviceName, Category category, CompanyProfile company) {
        this.serviceName = serviceName;
        this.setCategory(category);
        this.setCompany(company);
    }

    @Column(nullable = false, unique = true)
    private String serviceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyProfile company;

    public static ServiceOffering create(String serviceName, Category category, CompanyProfile company) {
        return new ServiceOffering(serviceName, category, company);
    }

    private void setCategory(Category category) {
        if (this.category != null)
            this.category.removeService(this);

        this.category = category;

        if (category != null)
            category.addService(this);
    }

    private void setCompany(CompanyProfile company) {
        if (this.company != null)
            this.company.removeService(this);

        this.company = company;

        if (company != null)
            company.addService(this);
    }
}
