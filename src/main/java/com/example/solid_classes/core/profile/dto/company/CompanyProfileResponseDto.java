package com.example.solid_classes.core.profile.dto.company;

import java.util.UUID;

import lombok.Builder;

@Builder
public class CompanyProfileResponseDto {
    private UUID id;
    private String companyName;
    private String cnpj;
}
