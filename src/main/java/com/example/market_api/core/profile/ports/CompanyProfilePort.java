package com.example.market_api.core.profile.ports;

import java.util.List;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;

public interface CompanyProfilePort extends NamedCrudPort<CompanyProfile>{
    
    CompanyProfile getByCnpj(String cnpj);
    List<CompanyProfile> findByBusinessSector(BusinessSector businessSector);
}
