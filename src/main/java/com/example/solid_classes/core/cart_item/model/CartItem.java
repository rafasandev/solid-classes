package com.example.solid_classes.core.cart_item.model;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.product.model.Product;

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
@Table(name = "cart_items")
@Getter
@SuperBuilder
@NoArgsConstructor
public class CartItem extends AuditableEntity{

    private int productQuantity;
    
    @Column(nullable = false)
    private double unitPriceSnapshot;
    
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    public void addQuantity(int quantity) {
        if (quantity > 0) {
            this.productQuantity += quantity;
        }
    }

    public void removeQuantity(int quantity) {
        if (quantity > 0 && this.productQuantity >= quantity) {
            this.productQuantity -= quantity;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.productQuantity = quantity;
        }
    }

    public double calculateSubtotal() {
        return this.unitPriceSnapshot * this.productQuantity;
    }
}
