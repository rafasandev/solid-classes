package com.example.market_api.core.payment_method.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.payment_method.model.PaymentMethod;
import com.example.market_api.core.payment_method.ports.PaymentMethodPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

	private final PaymentMethodPort paymentMethodPort;

	public Set<PaymentMethod> getAllByIds(Set<UUID> paymentMethodIds) {
		if (paymentMethodIds == null || paymentMethodIds.isEmpty()) {
			throw new BusinessRuleException("Informe ao menos um método de pagamento");
		}

		List<PaymentMethod> methods = paymentMethodPort.findAllByIds(paymentMethodIds);
		if (methods.size() != paymentMethodIds.size()) {
			throw new BusinessRuleException("Um ou mais métodos de pagamento informados são inválidos");
		}

		return new HashSet<>(methods);
	}
}
