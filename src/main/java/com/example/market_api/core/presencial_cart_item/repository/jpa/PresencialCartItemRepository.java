package com.example.market_api.core.presencial_cart_item.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;

public interface PresencialCartItemRepository extends JpaRepository<PresencialCartItem, UUID> {

	List<PresencialCartItem> findByPresencialCartId(UUID presencialCartId);

	Optional<PresencialCartItem> findByPresencialCartIdAndProductVariationId(UUID presencialCartId, UUID productVariationId);

	void deleteByPresencialCartId(UUID presencialCartId);
}
