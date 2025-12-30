package com.example.market_api.core.presencial_cart_item.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;

public interface PresencialCartItemPort extends NamedCrudPort<PresencialCartItem> {

	List<PresencialCartItem> findByCartId(UUID presencialCartId);

	Optional<PresencialCartItem> findByCartIdAndVariation(UUID cartId, UUID variationId);

	void deleteAllByCartId(UUID cartId);
}
