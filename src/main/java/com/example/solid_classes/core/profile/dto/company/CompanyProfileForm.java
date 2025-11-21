package com.example.solid_classes.core.profile.dto.company;

import com.example.solid_classes.core.user.dto.UserRegisterForm;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CompanyProfileForm extends UserRegisterForm {

    @NotEmpty(message = "O nome da empresa não pode estar vazio")
    private String companyName;

    @NotEmpty(message = "O CNPJ deve ser informado")
    private String cnpj;
}
