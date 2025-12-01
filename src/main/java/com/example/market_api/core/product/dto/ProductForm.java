package com.example.market_api.core.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductForm {
    
    @NotBlank(message = "O nome do produto não pode estar vazio")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    private String productName;
    
    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    private String productDescription;
    
    @NotNull(message = "O preço base é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal priceBase;
    
    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade deve ser maior ou igual a zero")
    private Integer stockQuantity;
    
    @NotNull(message = "A categoria é obrigatória")
    private UUID categoryId;
}
