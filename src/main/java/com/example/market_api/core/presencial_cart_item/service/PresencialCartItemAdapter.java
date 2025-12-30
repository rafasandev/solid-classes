package com.example.market_api.core.presencial_cart_item.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.presencial_cart_item.ports.PresencialCartItemPort;
import com.example.market_api.core.presencial_cart_item.repository.jpa.PresencialCartItemRepository;

@Component
public class PresencialCartItemAdapter extends NamedCrudAdapter<PresencialCartItem, PresencialCartItemRepository>
        implements PresencialCartItemPort {

    public PresencialCartItemAdapter(PresencialCartItemRepository repository) {
        super(repository, "Item de Carrinho Presencial");
    }

    @Override
    public List<PresencialCartItem> findByCartId(UUID presencialCartId) {
        return repository.findByPresencialCartId(presencialCartId);
    }

    @Override
    public Optional<PresencialCartItem> findByCartIdAndVariation(UUID cartId, UUID variationId) {
        return repository.findByPresencialCartIdAndProductVariationId(cartId, variationId);
    }

    @Override
    public void deleteAllByCartId(UUID cartId) {
        repository.deleteByPresencialCartId(cartId);
    }
}
