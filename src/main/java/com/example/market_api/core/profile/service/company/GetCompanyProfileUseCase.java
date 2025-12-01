package com.example.market_api.core.profile.service.company;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.mapper.ProfileMapper;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCompanyProfileUseCase {

    private final CompanyProfileService companyProfileService;
    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public List<CompanyProfileResponseDto> getAllCompanies() {
        List<CompanyProfile> companies = companyProfileService.findAll();
        return companies.stream()
            .map(profileMapper::toResponseDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public CompanyProfileResponseDto getCompanyById(UUID id) {
        CompanyProfile company = companyProfileService.getById(id);
        return profileMapper.toResponseDto(company);
    }

    @Transactional(readOnly = true)
    public CompanyProfileResponseDto getCompanyByCnpj(String cnpj) {
        CompanyProfile company = companyProfileService.getByCnpj(cnpj);
        return profileMapper.toResponseDto(company);
    }

    @Transactional(readOnly = true)
    public List<CompanyProfileResponseDto> getCompaniesByBusinessSector(BusinessSector businessSector) {
        List<CompanyProfile> companies = companyProfileService.findByBusinessSector(businessSector);
        return companies.stream()
            .map(profileMapper::toResponseDto)
            .toList();
    }
}
