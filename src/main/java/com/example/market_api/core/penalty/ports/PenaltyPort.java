package com.example.market_api.core.penalty.ports;

import java.util.List;
import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.penalty.model.Penalty;

public interface PenaltyPort extends NamedCrudPort<Penalty> {
    
    List<Penalty> findByProfileId(UUID profileId);

    List<Penalty> findByCompanyId(UUID companyId);
}
