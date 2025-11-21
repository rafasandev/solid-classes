package com.example.solid_classes.core.product.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String categoryName;
    private String companyName;
}
