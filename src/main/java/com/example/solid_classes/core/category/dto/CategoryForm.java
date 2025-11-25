package com.example.solid_classes.core.category.dto;

import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryForm {
    
    @NotBlank(message = "O nome da categoria não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String categoryName;

    @NotBlank(message = "O ramo de negócios da categoria deve ser fornecido")
    private BusinessSector businessSector;
}
