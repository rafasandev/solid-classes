package com.example.market_api.core.profile.model.company;

import java.util.List;
import java.util.Map;

import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.profile.model.ProfileEntity;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.model.company.enums.PaymentMethod;
import com.example.market_api.core.profile.model.value.TimeRange;
import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @Column(nullable = false)
    private List<Integer> weekDaysAvailable;

    @Column(nullable = false)
    private Map<Integer, List<TimeRange>> dailyAvailableTimeRanges;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VariationCategorySeller> variationCategories;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "company_payment_methods",
        joinColumns = @JoinColumn(name = "company_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private List<PaymentMethod> paymentMethods;

    public void addVariationCategory(VariationCategorySeller variationCategory) {
        if (variationCategory != null && this.variationCategories != null
                && !this.variationCategories.contains(variationCategory))
            this.variationCategories.add(variationCategory);
            variationCategory.setCompany(this);
    }

    public void removeVariationCategory(VariationCategorySeller variationCategory) {
        if (variationCategory != null && this.variationCategories != null
                && this.variationCategories.contains(variationCategory))
            this.variationCategories.remove(variationCategory);
            variationCategory.setCompany(null);
    }
}
