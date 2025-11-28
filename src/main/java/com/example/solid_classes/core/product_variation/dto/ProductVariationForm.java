package com.example.solid_classes.core.product_variation.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.solid_classes.core.product_variation.model.enums.VariationValueType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductVariationForm {
    
    @NotBlank(message = "O valor da variação não pode estar vazio")
    private String variationValue;
    
    @NotNull(message = "O tipo do valor é obrigatório")
    private VariationValueType valueType;
    
    @NotNull(message = "O preço adicional é obrigatório")
    @DecimalMin(value = "0.0", message = "O preço adicional deve ser maior ou igual a zero")
    private BigDecimal variationAdditionalPrice;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
    private int stockQuantity;
    
    @NotNull(message = "A categoria de variação é obrigatória")
    private UUID variationCategoryId;
    
    @NotNull(message = "O produto é obrigatório")
    private UUID productId;
}
