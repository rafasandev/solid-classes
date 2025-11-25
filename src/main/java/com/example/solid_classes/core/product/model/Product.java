package com.example.solid_classes.core.product.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.order_item.model.OrderItem;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "products")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Product extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 255)
    private String productName;

    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private double priceBase;
    
    @Column(nullable = false)
    private boolean available;
    
    @Column(nullable = false)
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyProfile company;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductVariation> productVariations;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public void setCategory(Category category) {
        if (this.category != null) {
            this.category.removeProduct(this);
        }

        this.category = category;

        if (category != null) {
            category.addProduct(this);
        }
    }

    public void setCompany(CompanyProfile company) {
        if (this.company != null) {
            this.company.removeProduct(this);
        }

        this.company = company;

        if (company != null) {
            company.addProduct(this);
        }
    }

    public void addProductVariation(ProductVariation productVariation) {
        if (productVariation != null && this.productVariations != null) {
            this.productVariations.add(productVariation);
        }
    }

    public void removeProductVariation(ProductVariation productVariation) {
        if (productVariation != null && this.productVariations != null) {
            this.productVariations.remove(productVariation);
        }
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null && this.orderItems != null) {
            this.orderItems.add(orderItem);
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if(orderItem != null && this.orderItems != null) {
            this.orderItems.remove(orderItem);
        }
    }

    public void decreaseStock(int quantity) {
        if (quantity > 0 && this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            if (this.stockQuantity == 0) {
                this.available = false;
            }
        }
    }

    public void increaseStock(int quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
            this.available = true;
        }
    }

    public boolean hasStock(int quantity) {
        return this.available && this.stockQuantity >= quantity;
    }

    public void toggleAvailability() {
        this.available = !this.available;
    }

}
