package com.example.solid_classes.core.service_offering.mapper;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.service_offering.dto.ServiceOfferingForm;
import com.example.solid_classes.core.service_offering.dto.ServiceOfferingResponseDto;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;

@Component
public class ServiceOfferingMapper {

    public ServiceOffering toEntity(ServiceOfferingForm serviceForm, Category category, CompanyProfile company) {
        ServiceOffering service = ServiceOffering.builder()
                .serviceName(serviceForm.getServiceName())
                .category(category)
                .company(company)
                .build();
        return service;
    }

    public ServiceOfferingResponseDto toResponseDto(ServiceOffering service) {
        ServiceOfferingResponseDto responseDto = ServiceOfferingResponseDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .categoryName(service.getCategory().getCategoryName())
                .companyName(service.getCompany().getCompanyName())
                .build();
        return responseDto;
    }
}
