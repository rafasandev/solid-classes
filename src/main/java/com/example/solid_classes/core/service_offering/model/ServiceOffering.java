package com.example.solid_classes.core.service_offering.model;

import com.example.solid_classes.common.base.AuditableEntity;
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
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "services")
@Getter
@SuperBuilder
@NoArgsConstructor
public class ServiceOffering extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String serviceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyProfile company;

    public void setCategory(Category category) {
        if (this.category != null)
            this.category.removeService(this);

        this.category = category;

        if (category != null)
            category.addService(this);
    }

    public void setCompany(CompanyProfile company) {
        if (this.company != null)
            this.company.removeService(this);

        this.company = company;

        if (company != null)
            company.addService(this);
    }
}
