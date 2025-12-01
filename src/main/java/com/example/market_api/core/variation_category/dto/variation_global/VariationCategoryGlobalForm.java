package com.example.market_api.core.variation_category.dto.variation_global;

import com.example.market_api.core.variation_category.model.enums.MeasureUnit;
import com.example.market_api.core.variation_category.model.enums.VariationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VariationCategoryGlobalForm {

    @NotBlank(message = "O nome da variação não pode estar vazio")
    @Size(min = 2, max = 120, message = "O nome da variação deve ter entre 2 e 120 caracteres")
    private String variationName;

    @NotNull(message = "A unidade de medida deve ser informada")
    private MeasureUnit measureUnit;

    @NotNull(message = "O tipo de variação deve ser informado")
    private VariationType type;

    @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres")
    private String variationDescription;
}
