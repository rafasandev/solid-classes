package com.example.solid_classes.core.service_offering.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.category.service.CategoryService;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.service.company.CompanyProfileService;
import com.example.solid_classes.core.service_offering.dto.ServiceOfferingForm;
import com.example.solid_classes.core.service_offering.dto.ServiceOfferingResponseDto;
import com.example.solid_classes.core.service_offering.mapper.ServiceOfferingMapper;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterServiceOfferingUseCase {

    private final ServiceOfferingService serviceOfferingService;
    private final ServiceOfferingMapper serviceOfferingMapper;
    private final CategoryService categoryService;
    private final CompanyProfileService companyProfileService;

    @Transactional
    public ServiceOfferingResponseDto registerServiceOffering(ServiceOfferingForm serviceForm) {
        Category category = categoryService.getById(serviceForm.getCategoryId());
        CompanyProfile company = companyProfileService.getById(serviceForm.getCompanyId());

        ServiceOffering newService = serviceOfferingMapper.toEntity(serviceForm, category, company);
        ServiceOffering savedService = serviceOfferingService.createService(newService);
        ServiceOfferingResponseDto responseDto = serviceOfferingMapper.toResponseDto(savedService);
        return responseDto;        
    }
}
