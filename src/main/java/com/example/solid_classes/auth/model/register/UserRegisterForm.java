package com.example.solid_classes.auth.model.register;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserRegisterForm {

    @NotEmpty(message = "O email não pode estar vazio")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotEmpty(message = "A senha não pode estar vazia")
    @Length(min = 8, message = "A senha deve possuir ao menos 8 caracteres")
    private String password;

    @NotEmpty(message = "O nome não pode estar vazio")
    private String name;
}
