package com.example.market_api.core.service_offering.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.service_offering.dto.ServiceOfferingForm;
import com.example.market_api.core.service_offering.dto.ServiceOfferingResponseDto;
import com.example.market_api.core.service_offering.model.ServiceOffering;

@Component
public class ServiceOfferingMapper {

    public ServiceOffering toEntity(ServiceOfferingForm serviceForm, Category category, CompanyProfile company) {
        ServiceOffering service = ServiceOffering.builder()
                .serviceName(serviceForm.getServiceName())
                .description(serviceForm.getDescription())
                .price(serviceForm.getPrice())
                .available(true)
                .categoryId(category.getId())
                .companyId(company.getId())
                .build();
        return service;
    }

    public ServiceOfferingResponseDto toResponseDto(ServiceOffering service, Category category, CompanyProfile company) {
        ServiceOfferingResponseDto responseDto = ServiceOfferingResponseDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .categoryName(category.getCategoryName())
                .companyName(company.getCompanyName())
                .build();
        return responseDto;
    }
}
