package com.example.solid_classes.core.profile.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.profile.model.company.CompanyProfile;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, UUID>{
    Optional<CompanyProfile> findByCnpj(String cnpj);
}
