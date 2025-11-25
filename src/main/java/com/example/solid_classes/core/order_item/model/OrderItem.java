package com.example.solid_classes.core.order_item.model;

import java.math.BigDecimal;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.product.model.Product;

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

    private String productName;
    private BigDecimal productPrice;
    private int productQuantity;
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.removeOrderItem(this);
        }

        this.order = order;

        if (order != null) {
            order.addOrderItem(this);
        }
    }

    public void setProduct(Product product) {
        if (this.product != null) {
            this.product.removeOrderItem(this);
        }

        this.product = product;

        if (product != null) {
            product.addOrderItem(this);
        }
    }
}
