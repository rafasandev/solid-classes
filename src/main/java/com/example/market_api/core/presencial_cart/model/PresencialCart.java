package com.example.market_api.core.presencial_cart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "presencial_carts")
@Getter
@SuperBuilder
@NoArgsConstructor
public class PresencialCart extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyProfile seller;

    @ManyToOne(fetch = FetchType.LAZY)
    private IndividualProfile buyer;

    @Column(length = 120)
    private String buyerName;

    @Column(length = 20)
    private String buyerDocument;

    @Column(nullable = false)
    private boolean finalized;

    private LocalDateTime finalizedAt;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "presencialCart", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PresencialCartItem> items;

    public void addItem(PresencialCartItem item) {
        if (item == null) {
            return;
        }
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        if (!this.items.contains(item)) {
            this.items.add(item);
            item.setPresencialCart(this);
        }
    }

    public void removeItem(PresencialCartItem item) {
        if (item == null || this.items == null) {
            return;
        }
        if (this.items.remove(item)) {
            item.setPresencialCart(null);
        }
    }

    public void markAsFinalized(Order order) {
        this.finalized = true;
        this.finalizedAt = LocalDateTime.now();
        this.order = order;
    }

    public BigDecimal calculateItemsTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(PresencialCartItem::getSubtotalSnapshot)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
