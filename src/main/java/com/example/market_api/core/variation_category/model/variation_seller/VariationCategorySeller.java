package com.example.market_api.core.variation_category.model.variation_seller;

import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.variation_category.model.VariationCategoryEntity;

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
@Table(name = "seller_variations")
@Getter
@SuperBuilder
@NoArgsConstructor
public class VariationCategorySeller extends VariationCategoryEntity {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyProfile company;

}
