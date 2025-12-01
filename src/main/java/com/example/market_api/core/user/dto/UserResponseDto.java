package com.example.market_api.core.user.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class UserResponseDto {
    private UUID id;
    private String email;
}
