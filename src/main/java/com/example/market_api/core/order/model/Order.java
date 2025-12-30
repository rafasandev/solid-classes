package com.example.market_api.core.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.order.model.enums.OrderStatus;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

import jakarta.persistence.CascadeType;
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

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTotal;

    @Column(nullable = false)
    private boolean isPaid;

    private LocalDateTime paidAt;

    @Setter
    @OneToMany(mappedBy = "order", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private IndividualProfile customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private CompanyProfile company;

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null && this.orderItems != null && !this.orderItems.contains(orderItem)) {
            this.orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItem != null && this.orderItems != null) {
            this.orderItems.remove(orderItem);
            orderItem.setOrder(null);
        }
    }
}
