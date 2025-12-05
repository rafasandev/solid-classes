package com.example.market_api.core.cart_item.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.cart_item.model.enums.ReservationStatus;

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
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "cart_items",
    // Unique per cart + specific product variation (allows multiple variations of same product)
    uniqueConstraints = @UniqueConstraint(columnNames = { "cart_id", "product_variation_id" }),
    indexes = {
        @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
        @Index(name = "idx_cart_item_product_variation", columnList = "product_variation_id")
    })
@Getter
@SuperBuilder
@NoArgsConstructor
public class CartItem extends AuditableEntity {

    private UUID productVariationId;
    private UUID productId;
    private String productName;
    
    @Column(nullable = false)
    private int itemQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;
    
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public void addQuantity(int quantity) {
        if (quantity > 0 && this.status == ReservationStatus.PENDING) {
            this.itemQuantity += quantity;
        }
    }

    public void removeQuantity(int quantity) {
        if (quantity > 0 && this.itemQuantity >= quantity && this.status == ReservationStatus.PENDING) {
            this.itemQuantity -= quantity;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity > 0 && this.status == ReservationStatus.PENDING) {
            this.itemQuantity = quantity;
        }
    }

    public BigDecimal calculateSubtotal() {
        return this.unitPriceSnapshot.multiply(BigDecimal.valueOf(this.itemQuantity));
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
