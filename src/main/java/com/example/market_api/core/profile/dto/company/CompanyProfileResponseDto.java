package com.example.market_api.core.profile.dto.company;

import java.util.UUID;

import com.example.market_api.core.profile.model.company.enums.BusinessSector;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyProfileResponseDto {
    private UUID id;
    private String companyName;
    private String cnpj;
    private BusinessSector businessSector;
}
