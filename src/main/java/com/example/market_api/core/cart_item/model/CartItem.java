package com.example.market_api.core.cart_item.model;

import java.util.UUID;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.cart.model.Cart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
// Indexação para melhorar performance em consultas por cart_id e product_variation_id
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = { "cart_id", "product_variation_id" }), indexes = {
                @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
                @Index(name = "idx_cart_item_product_variation", columnList = "product_variation_id")
        })
@Getter
@SuperBuilder
@NoArgsConstructor
public class CartItem extends AuditableEntity {

    @Column(nullable = false)
    private int itemQuantity;

    private UUID productVariationId;
    private UUID productId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public void addQuantity(int quantity) {
        if (quantity > 0) {
            this.itemQuantity += quantity;
        }
    }

    public void removeQuantity(int quantity) {
        if (quantity > 0 && this.itemQuantity >= quantity) {
            this.itemQuantity -= quantity;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.itemQuantity = quantity;
        }
    }

}
