package com.example.market_api.core.profile.dto.company;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CompanyPaymentMethodsForm {

    @NotEmpty(message = "Informe pelo menos um método de pagamento aceito")
    private Set<UUID> paymentMethodIds;
}
