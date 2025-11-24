package com.example.solid_classes.core.cart.model;

import java.util.List;

import com.example.solid_classes.common.base.AuditableEntity;
import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;

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
    @JoinColumn(name = "profile_id")
    private IndividualProfile profile;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> items;

    public void addCartItem(CartItem cartItem) {
        this.items.add(cartItem);
    }
}
