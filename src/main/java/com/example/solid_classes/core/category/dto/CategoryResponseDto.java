package com.example.solid_classes.core.category.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public class CategoryResponseDto {
    private UUID id;
    private String name;
}
