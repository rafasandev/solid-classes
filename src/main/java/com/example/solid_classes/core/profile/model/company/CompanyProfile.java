package com.example.solid_classes.core.profile.model.company;

import java.util.List;

import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.profile.model.ProfileEntity;
import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;
import com.example.solid_classes.core.variation_category.model.variation_seller.VariationCategorySeller;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessSector businessSector;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VariationCategorySeller> variationCategories;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    public void addVariationCategory(VariationCategorySeller variationCategory) {
        if (variationCategory != null && this.variationCategories != null
                && !this.variationCategories.contains(variationCategory))
            this.variationCategories.add(variationCategory);
    }

    public void removeVariationCategory(VariationCategorySeller variationCategory) {
        if (variationCategory != null && this.variationCategories != null
                && this.variationCategories.contains(variationCategory))
            this.variationCategories.remove(variationCategory);
    }
}
