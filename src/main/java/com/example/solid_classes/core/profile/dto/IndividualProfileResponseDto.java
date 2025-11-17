package com.example.solid_classes.core.profile.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class IndividualProfileResponseDto {
    private UUID id;
    private String name;
    private String cpf;
}
