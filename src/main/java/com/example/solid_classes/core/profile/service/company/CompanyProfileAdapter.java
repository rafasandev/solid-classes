package com.example.solid_classes.core.profile.service.company;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;
import com.example.solid_classes.core.profile.ports.CompanyProfilePort;
import com.example.solid_classes.core.profile.repository.jpa.CompanyProfileRepository;

@Component
public class CompanyProfileAdapter extends NamedCrudAdapter<CompanyProfile, CompanyProfileRepository>
        implements CompanyProfilePort {

    public CompanyProfileAdapter(CompanyProfileRepository companyProfileRepository) {
        super(companyProfileRepository, "Perfil de Empresa");
    }

    @Override
    public CompanyProfile getByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj).orElseThrow(this::throwEntityNotFound);
    }

    @Override
    public List<CompanyProfile> findByBusinessSector(BusinessSector businessSector) {
        return repository.findByBusinessSector(businessSector);
    }
}
