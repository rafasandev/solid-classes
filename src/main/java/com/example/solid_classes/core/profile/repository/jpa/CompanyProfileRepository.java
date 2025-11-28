package com.example.solid_classes.core.profile.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.model.company.enums.BusinessSector;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, UUID>{
    
    Optional<CompanyProfile> findByCnpj(String cnpj);
    List<CompanyProfile> findByBusinessSector(BusinessSector businessSector);
}
