package com.example.solid_classes.core.service_offering.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ServiceOfferingForm {
    private String serviceName;
    private UUID categoryId;
    private UUID companyId;
}
