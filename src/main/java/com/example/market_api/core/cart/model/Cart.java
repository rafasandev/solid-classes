package com.example.market_api.core.cart.model;

import java.util.List;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.cart_item.model.CartItem;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "carts")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Cart extends AuditableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private IndividualProfile profile;

    @OneToMany(mappedBy = "cart", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> items;

    public void addCartItem(CartItem cartItem) {
        if (cartItem != null && this.items != null && !this.items.contains(cartItem)) {
            this.items.add(cartItem);
            cartItem.setCart(this);
        }
    }

    public void removeCartItem(CartItem cartItem) {
        if (cartItem != null && this.items != null) {
            this.items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public int getTotalItems() {
        return this.items == null
                ? 0
                : this.items.stream()
                        .mapToInt(CartItem::getItemQuantity)
                        .sum();
    }
}
