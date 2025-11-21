package com.example.solid_classes.core.variation_category.model.variation_seller;

import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "seller_variations")
@Getter
@SuperBuilder
@NoArgsConstructor
public class VariationCategorySeller extends VariationCategoryEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyProfile company;

}
