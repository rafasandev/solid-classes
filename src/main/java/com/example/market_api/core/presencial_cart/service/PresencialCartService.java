package com.example.market_api.core.presencial_cart.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.presencial_cart.ports.PresencialCartPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresencialCartService {

    private final PresencialCartPort presencialCartPort;

    public PresencialCart getById(UUID id) {
        return presencialCartPort.getById(id);
    }

    public PresencialCart getByIdAndSeller(UUID cartId, UUID sellerId) {
        return presencialCartPort.getByIdAndSeller(cartId, sellerId);
    }

    public Page<PresencialCart> findBySeller(UUID sellerId, Pageable pageable) {
        return presencialCartPort.findBySeller(sellerId, pageable);
    }

    public PresencialCart save(PresencialCart presencialCart) {
        return presencialCartPort.save(presencialCart);
    }

    public void delete(PresencialCart presencialCart) {
        presencialCartPort.delete(presencialCart);
    }
}
