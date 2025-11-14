package com.example.solid_classes.core.service_offering.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.category.interfaces.CategoryPort;
import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.profile.interfaces.CompanyProfilePort;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.service_offering.dto.ServiceOfferingForm;
import com.example.solid_classes.core.service_offering.interfaces.ServiceOfferingPort;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService {

    private final ServiceOfferingPort serviceOfferingPort;
    private final CategoryPort categoryPort;
    private final CompanyProfilePort companyPort;

    @Transactional
    public ServiceOffering createService(ServiceOfferingForm serviceForm) {
        Category category = categoryPort.getById(serviceForm.getCategoryId());
        CompanyProfile company = companyPort.getById(serviceForm.getCompanyId());

        ServiceOffering newService = ServiceOffering.create(
                serviceForm.getServiceName(),
                category,
                company);

        return serviceOfferingPort.save(newService);
    }
}
