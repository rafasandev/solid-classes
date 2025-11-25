package com.example.solid_classes.core.cart_item.model;

import java.math.BigDecimal;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.cart_item.model.enums.ReservationStatus;
import com.example.solid_classes.core.product.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = { "cart_id",
        "product_id" }), indexes = {
                @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
                @Index(name = "idx_cart_item_product", columnList = "product_id")
        })
@Getter
@SuperBuilder
@NoArgsConstructor
public class CartItem extends AuditableEntity {

    @Column(nullable = false)
    private int productQuantity;

    @Column(nullable = false)
    private BigDecimal unitPriceSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private Cart cart;

    public void addQuantity(int quantity) {
        if (quantity > 0 && this.status == ReservationStatus.PENDING) {
            this.productQuantity += quantity;
        }
    }

    public void removeQuantity(int quantity) {
        if (quantity > 0 && this.productQuantity >= quantity && this.status == ReservationStatus.PENDING) {
            this.productQuantity -= quantity;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity > 0 && this.status == ReservationStatus.PENDING) {
            this.productQuantity = quantity;
        }
    }

    public BigDecimal calculateSubtotal() {
        return this.unitPriceSnapshot.multiply(BigDecimal.valueOf(this.productQuantity));
    }

    public void reserve() {
        if (this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.RESERVED;
        }
    }

    public void complete() {
        if (this.status == ReservationStatus.RESERVED) {
            this.status = ReservationStatus.COMPLETED;
        }
    }

    public void cancel() {
        if (this.status == ReservationStatus.PENDING || this.status == ReservationStatus.RESERVED) {
            this.status = ReservationStatus.CANCELLED;
        }
    }

    public boolean canModifyQuantity() {
        return this.status == ReservationStatus.PENDING;
    }
}
