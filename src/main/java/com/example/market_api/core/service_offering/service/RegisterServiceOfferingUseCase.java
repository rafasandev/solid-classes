package com.example.market_api.core.service_offering.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.category.model.Category;
import com.example.market_api.core.category.service.CategoryService;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.service_offering.dto.ServiceOfferingForm;
import com.example.market_api.core.service_offering.dto.ServiceOfferingResponseDto;
import com.example.market_api.core.service_offering.mapper.ServiceOfferingMapper;
import com.example.market_api.core.service_offering.model.ServiceOffering;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterServiceOfferingUseCase {

    private final ServiceOfferingService serviceOfferingService;
    private final ServiceOfferingMapper serviceOfferingMapper;
    private final CategoryService categoryService;
    private final CompanyProfileService companyProfileService;
    private final UserService userService;

    @Transactional
    public ServiceOfferingResponseDto registerServiceOffering(ServiceOfferingForm serviceForm) {
        Category category = categoryService.getById(serviceForm.getCategoryId());
        CompanyProfile company = companyProfileService.getById(serviceForm.getCompanyId());

        // Ensure the caller is the owner of the company
        User loggedUser = userService.getLoggedInUser();
        if (company == null || !company.getId().equals(loggedUser.getId())) {
            throw new BusinessRuleException("Usuário logado não é o proprietário da empresa associada ao serviço");
        }

        if(!company.isActive())
            throw new BusinessRuleException("Empresa inativa. Operação negada");

        if(company.getBusinessSector() != BusinessSector.SERVICE)
            throw new BusinessRuleException("Ramo da empresa apenas permite operações com serviços");

        if(category.getBusinessSector() != BusinessSector.SERVICE)
            throw new BusinessRuleException("Categoria não é compatível com serviços. Use categoria de serviços");

        ServiceOffering newService = serviceOfferingMapper.toEntity(serviceForm, category, company);
        ServiceOffering savedService = serviceOfferingService.save(newService);
        ServiceOfferingResponseDto responseDto = serviceOfferingMapper.toResponseDto(savedService, category, company);
        return responseDto;
    }
}
