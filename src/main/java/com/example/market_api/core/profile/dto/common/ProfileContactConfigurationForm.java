package com.example.market_api.core.profile.dto.common;

import java.util.List;

import com.example.market_api.core.contact_info.dto.ContactInfoForm;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ProfileContactConfigurationForm {

    @NotEmpty(message = "Informe pelo menos um canal de contato")
    @Valid
    private List<ContactInfoForm> contacts;
}
