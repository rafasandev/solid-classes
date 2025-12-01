package com.example.market_api.core.category.dto;

import java.util.UUID;

import com.example.market_api.core.profile.model.company.enums.BusinessSector;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private BusinessSector businessSector;
}
