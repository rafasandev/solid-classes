package com.example.solid_classes.core.product.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ProductForm {
    private String productName;
    private UUID categoryId;
    private UUID companyId;
}
