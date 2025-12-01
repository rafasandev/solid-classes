package com.example.market_api.core.category.dto;

import com.example.market_api.core.profile.model.company.enums.BusinessSector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryForm {
    
    @NotBlank(message = "O nome da categoria não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String categoryName;

    @NotNull(message = "O ramo de negócios da categoria deve ser fornecido")
    private BusinessSector businessSector;
}
