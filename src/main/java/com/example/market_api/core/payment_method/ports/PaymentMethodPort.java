package com.example.market_api.core.payment_method.ports;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.payment_method.model.PaymentMethod;

public interface PaymentMethodPort extends NamedCrudPort<PaymentMethod> {

	List<PaymentMethod> findAllByIds(Set<UUID> ids);
}
