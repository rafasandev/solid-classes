package com.example.solid_classes.core.service.model;

import com.example.solid_classes.common.abs.AuditableEntity;
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
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "services")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Service extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String serviceName;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyProfile company;
}
