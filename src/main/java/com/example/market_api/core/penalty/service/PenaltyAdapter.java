package com.example.market_api.core.penalty.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedMongoAdapter;
import com.example.market_api.core.penalty.model.Penalty;
import com.example.market_api.core.penalty.ports.PenaltyPort;
import com.example.market_api.core.penalty.repository.mongo.PenaltyRepository;

@Component
public class PenaltyAdapter extends NamedMongoAdapter<Penalty, PenaltyRepository> implements PenaltyPort {
    
    public PenaltyAdapter(PenaltyRepository repository) {
        super(repository, "Penalidade");
    }

    @Override
    public List<Penalty> findByProfileId(UUID profileId) {
        return repository.findByProfileId(profileId);
    }
    
    @Override
    public List<Penalty> findByCompanyId(UUID companyId) {
        return repository.findByCompanyId(companyId);
    }
}
