package com.example.market_api.core.presencial_cart.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.presencial_cart.model.PresencialCart;

public interface PresencialCartRepository extends JpaRepository<PresencialCart, UUID> {

	@EntityGraph(attributePaths = { "items" })
	Page<PresencialCart> findBySellerIdOrderByCreatedAtDesc(UUID sellerId, Pageable pageable);

	@EntityGraph(attributePaths = { "items" })
	Optional<PresencialCart> findByIdAndSellerId(UUID id, UUID sellerId);
}
