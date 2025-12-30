package com.example.market_api.core.presencial_cart_item.model;

import java.util.UUID;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.presencial_cart.model.PresencialCart;

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
// Indexação para melhorar performance em consultas por presencial_cart_id e
// product_variation_id
@Table(name = "presencial_cart_items", uniqueConstraints = @UniqueConstraint(columnNames = { "presencial_cart_id",
        "product_variation_id" }), indexes = {
                @Index(name = "idx_presencial_cart_item_cart", columnList = "presencial_cart_id"),
                @Index(name = "idx_presencial_cart_item_product_variation", columnList = "product_variation_id")
        })
@Getter
@SuperBuilder
@NoArgsConstructor
public class PresencialCartItem extends AuditableEntity {

    @Column(nullable = false)
    private int itemQuantity;

    @Column(nullable = false)
    private UUID productVariationId;

    @Column(nullable = false)
    private UUID productId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presencial_cart_id", nullable = false)
    private PresencialCart presencialCart;

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.itemQuantity = quantity;
        }
    }
}
