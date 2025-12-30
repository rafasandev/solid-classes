package com.example.market_api.core.presencial_cart.ports;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.presencial_cart.model.PresencialCart;

public interface PresencialCartPort extends NamedCrudPort<PresencialCart> {

	Page<PresencialCart> findBySeller(UUID sellerId, Pageable pageable);

	PresencialCart getByIdAndSeller(UUID cartId, UUID sellerId);
}
