package com.example.solid_classes.core.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private int stockQuantity;
    private String companyName;
    private String categoryName;
}
