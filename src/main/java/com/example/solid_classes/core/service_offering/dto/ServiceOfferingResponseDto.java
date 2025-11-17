package com.example.solid_classes.core.service_offering.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public class ServiceOfferingResponseDto {
    private UUID id;
    private String serviceName;
    private String categoryName;
    private String companyName;
}
