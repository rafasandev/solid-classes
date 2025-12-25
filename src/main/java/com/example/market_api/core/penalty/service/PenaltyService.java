package com.example.market_api.core.penalty.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.market_api.core.penalty.model.Penalty;
import com.example.market_api.core.penalty.ports.PenaltyPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PenaltyService {
    
    private final PenaltyPort penaltyPort;

    public Penalty save(Penalty penalty) {
        return penaltyPort.save(penalty);
    }

    public List<Penalty> findByProfileId(java.util.UUID profileId) {
        return penaltyPort.findByProfileId(profileId);
    }

    public List<Penalty> findByCompanyId(java.util.UUID companyId) {
        return penaltyPort.findByCompanyId(companyId);
    }
}
