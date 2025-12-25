package com.example.market_api.core.penalty.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.market_api.core.penalty.model.Penalty;

public interface PenaltyRepository extends MongoRepository<Penalty, UUID> {
    
    List<Penalty> findByProfileId(UUID profileId);

    List<Penalty> findByCompanyId(UUID companyId);
}
