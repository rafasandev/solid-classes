package com.example.market_api.core.presencial_cart.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.presencial_cart.ports.PresencialCartPort;
import com.example.market_api.core.presencial_cart.repository.jpa.PresencialCartRepository;

@Component
public class PresencialCartAdapter extends NamedCrudAdapter<PresencialCart, PresencialCartRepository>
        implements PresencialCartPort {

    public PresencialCartAdapter(PresencialCartRepository repository) {
        super(repository, "Carrinho Presencial");
    }

    @Override
    public Page<PresencialCart> findBySeller(UUID sellerId, Pageable pageable) {
        return repository.findBySellerIdOrderByCreatedAtDesc(sellerId, pageable);
    }

    @Override
    public PresencialCart getByIdAndSeller(UUID cartId, UUID sellerId) {
        return repository.findByIdAndSellerId(cartId, sellerId)
                .orElseThrow(() -> throwEntityNotFound());
    }
}
