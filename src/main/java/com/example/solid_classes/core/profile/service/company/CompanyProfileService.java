package com.example.solid_classes.core.profile.service.company;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.ports.CompanyProfilePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfilePort companyPort;

    public CompanyProfile registerProfile(CompanyProfile newProfile) {
        return companyPort.save(newProfile);
    }

    public CompanyProfile getById(java.util.UUID companyId) {
        return companyPort.getById(companyId);
    }

}
