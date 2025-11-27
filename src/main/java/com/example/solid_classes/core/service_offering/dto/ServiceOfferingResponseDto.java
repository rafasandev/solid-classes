package com.example.solid_classes.core.service_offering.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class ServiceOfferingResponseDto {
    private UUID id;
    private String serviceName;
}
