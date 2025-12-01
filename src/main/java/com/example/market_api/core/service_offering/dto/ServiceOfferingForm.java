package com.example.market_api.core.service_offering.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ServiceOfferingForm {
    
    @NotBlank(message = "O nome do serviço não pode estar vazio")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    private String serviceName;
    
    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    private String description;
    
    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal price;
    
    @NotNull(message = "A categoria é obrigatória")
    private UUID categoryId;
    
    @NotNull(message = "A empresa é obrigatória")
    private UUID companyId;
}
