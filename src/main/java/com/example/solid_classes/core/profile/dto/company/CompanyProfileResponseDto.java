package com.example.solid_classes.core.profile.dto.company;

import java.util.UUID;

import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;

import lombok.Builder;

@Builder
public class CompanyProfileResponseDto {
    private UUID id;
    private String companyName;
    private String cnpj;
    private BusinessSector businessSector;
}
