package com.example.market_api.core.payment_method.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.payment_method.model.PaymentMethod;
import com.example.market_api.core.payment_method.ports.PaymentMethodPort;
import com.example.market_api.core.payment_method.repository.jpa.PaymentMethodRepository;

@Component
public class PaymentMethodAdapter extends NamedCrudAdapter<PaymentMethod, PaymentMethodRepository> implements PaymentMethodPort {
    
    public PaymentMethodAdapter(PaymentMethodRepository repository) {
        super(repository, "Método de Pagamento");
    }

    @Override
    public List<PaymentMethod> findAllByIds(Set<UUID> ids) {
        return repository.findAllById(ids);
    }
}
