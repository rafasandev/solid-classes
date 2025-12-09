package com.example.market_api.core.profile.model.individual;

import java.util.List;

import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.profile.model.ProfileEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "individual_profiles")
@Getter
@SuperBuilder
@NoArgsConstructor
public class IndividualProfile extends ProfileEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    @OneToOne(mappedBy = "profile")
    private Cart cart;

    @OneToMany(mappedBy = "customer", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;
}
