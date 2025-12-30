package com.example.market_api.core.presencial_cart_item.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.presencial_cart_item.ports.PresencialCartItemPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresencialCartItemService {

    private final PresencialCartItemPort presencialCartItemPort;

    public PresencialCartItem save(PresencialCartItem presencialCartItem) {
        return presencialCartItemPort.save(presencialCartItem);
    }

    public PresencialCartItem getById(UUID id) {
        return presencialCartItemPort.getById(id);
    }

    public List<PresencialCartItem> findByCartId(UUID cartId) {
        return presencialCartItemPort.findByCartId(cartId);
    }

    public Optional<PresencialCartItem> findByCartAndVariation(UUID cartId, UUID variationId) {
        return presencialCartItemPort.findByCartIdAndVariation(cartId, variationId);
    }

    public void delete(PresencialCartItem presencialCartItem) {
        presencialCartItemPort.delete(presencialCartItem);
    }

    public void deleteAllByCartId(UUID cartId) {
        presencialCartItemPort.deleteAllByCartId(cartId);
    }
}
