package com.example.solid_classes.core.user.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class UserResponseDto {
    private UUID id;
    private String email;
}
