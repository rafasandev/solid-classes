package com.example.market_api.core.profile.dto.individual;

import com.example.market_api.core.user.dto.UserRegisterForm;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class IndividualProfileForm extends UserRegisterForm {

    @NotEmpty(message = "O nome do usuário não pode estar vazio")
    private String name;

    @NotEmpty(message = "O CPF deve ser informado")
    private String cpf;
}
