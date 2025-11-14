package com.example.solid_classes.core.profile.interfaces;

import com.example.solid_classes.common.interfaces.NamedCrudPort;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;

public interface CompanyProfilePort extends NamedCrudPort<CompanyProfile>{
    
    public CompanyProfile getByCnpj(String cnpj);
}
