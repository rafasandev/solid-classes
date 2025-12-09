package com.example.market_api.core.profile.model.company;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.payment_method.model.PaymentMethod;
import com.example.market_api.core.profile.model.ProfileEntity;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.model.company.value.CompanyDailyAvailability;
import com.example.market_api.core.variation_category.model.variation_seller.VariationCategorySeller;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessSector businessSector;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "company_available_week_days", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "week_day", nullable = false)
    private List<Integer> weekDaysAvailable;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "company_daily_availability", joinColumns = @JoinColumn(name = "company_id"))
    private List<CompanyDailyAvailability> dailyAvailableTimeRanges;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VariationCategorySeller> variationCategories;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_payment_methods", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private Set<PaymentMethod> paymentMethods;

    public void setWeekDaysAvailable(List<Integer> weekDaysAvailable) {
        this.weekDaysAvailable = weekDaysAvailable;
    }

    public void setDailyAvailableTimeRanges(List<CompanyDailyAvailability> dailyAvailableTimeRanges) {
        this.dailyAvailableTimeRanges = dailyAvailableTimeRanges;
    }

    public void addVariationCategory(VariationCategorySeller variationCategory) {
        if (variationCategory != null
                && this.variationCategories != null
                && !this.variationCategories.contains(variationCategory)) {
            this.variationCategories.add(variationCategory);
            variationCategory.setCompany(this);
        }
    }

    public void removeVariationCategory(VariationCategorySeller variationCategory) {
        if (variationCategory != null
                && this.variationCategories != null
                && this.variationCategories.contains(variationCategory)) {
            this.variationCategories.remove(variationCategory);
            variationCategory.setCompany(null);
        }
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod != null
                && this.paymentMethods != null
                && !this.paymentMethods.contains(paymentMethod)) {
            this.paymentMethods.add(paymentMethod);
            paymentMethod.addCompanyProfile(this);
        }
    }

    public void removePaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod != null
                && this.paymentMethods != null
                && this.paymentMethods.contains(paymentMethod)) {
            this.paymentMethods.remove(paymentMethod);
            paymentMethod.removeCompanyProfile(this);
        }
    }
}
