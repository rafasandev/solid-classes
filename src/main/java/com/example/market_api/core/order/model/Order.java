package com.example.market_api.core.order.model;

import java.math.BigDecimal;
import java.util.List;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.order.model.enums.OrderStatus;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "orders")
@SuperBuilder
@Getter
@NoArgsConstructor
public class Order extends AuditableEntity {

    @Column(nullable = false)
    private String pickUpcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTotal;
    
    @Setter
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private IndividualProfile customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private CompanyProfile company;

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null && this.orderItems != null) {
            this.orderItems.add(orderItem);
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItem != null && this.orderItems != null) {
            this.orderItems.remove(orderItem);
        }
    }
}
