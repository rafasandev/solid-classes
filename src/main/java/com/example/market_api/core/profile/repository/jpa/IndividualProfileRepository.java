package com.example.market_api.core.profile.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.profile.model.individual.IndividualProfile;

public interface IndividualProfileRepository extends JpaRepository<IndividualProfile, UUID>{
    Optional<IndividualProfile> findByCpf(String cpf);
}
