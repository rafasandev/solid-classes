package com.example.market_api.core.order_item.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.order.model.Order;

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
@Table(name = "order_items")
@SuperBuilder
@Getter
@NoArgsConstructor
public class OrderItem extends AuditableEntity {

    @Column(nullable = false)
    private UUID productId;

    private UUID productVariationId;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(length = 255)
    private String productVariationValue;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal variationAdditionalPriceSnapshot;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal finalUnitPriceSnapshot;

    @Column(nullable = false)
    private int orderQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.removeOrderItem(this);
        }

        this.order = order;

        if (order != null) {
            order.addOrderItem(this);
        }
    }

    public void calculateSubtotal() {
        this.subtotal = finalUnitPriceSnapshot.multiply(BigDecimal.valueOf(orderQuantity));
    }
}
