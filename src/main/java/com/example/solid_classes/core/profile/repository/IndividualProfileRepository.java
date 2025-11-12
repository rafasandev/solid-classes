package com.example.solid_classes.core.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.profile.model.individual.IndividualProfile;

public interface IndividualProfileRepository extends JpaRepository<IndividualProfile, UUID>{
    
}
