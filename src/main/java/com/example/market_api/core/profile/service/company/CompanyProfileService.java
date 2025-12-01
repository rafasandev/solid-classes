package com.example.market_api.core.profile.service.company;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.ports.CompanyProfilePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfilePort companyProfilePort;

    public CompanyProfile getById(UUID id) {
        return companyProfilePort.getById(id);
    }

    public CompanyProfile getByCnpj(String cnpj) {
        return companyProfilePort.getByCnpj(cnpj);
    }

    public CompanyProfile save(CompanyProfile company) {
        return companyProfilePort.save(company);
    }

    public List<CompanyProfile> findAll() {
        return companyProfilePort.findAll();
    }

    public List<CompanyProfile> findByBusinessSector(BusinessSector businessSector) {
        return companyProfilePort.findByBusinessSector(businessSector);
    }

    public void validateIsActive(CompanyProfile company) {
        if (!company.isActive()) {
            throw new BusinessRuleException(
                    String.format("Empresa '%s' está inativa. Operação negada",
                            company.getCompanyName()));
        }
    }

    public void validateBusinessSector(CompanyProfile company, BusinessSector expectedSector) {
        if (company.getBusinessSector() != expectedSector) {
            throw new BusinessRuleException(
                    String.format("Empresa '%s' opera no setor %s, mas a operação requer setor %s",
                            company.getCompanyName(),
                            company.getBusinessSector(),
                            expectedSector));
        }
    }
}
