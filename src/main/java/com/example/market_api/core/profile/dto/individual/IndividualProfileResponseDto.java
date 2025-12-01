package com.example.market_api.core.profile.dto.individual;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndividualProfileResponseDto {
    private UUID id;
    private String name;
    private String cpf;
}
