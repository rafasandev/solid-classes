package com.example.solid_classes.core.product.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
}
