package com.example.market_api.core.presencial_cart.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PresencialCartForm {
    @Pattern(regexp = "^\\d{11}$", message = "CPF do comprador deve conter 11 dígitos numéricos")
    private String clientCpf;
}
