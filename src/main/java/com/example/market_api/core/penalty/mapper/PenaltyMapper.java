package com.example.market_api.core.penalty.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.penalty.dto.PenaltyInternalForm;
import com.example.market_api.core.penalty.model.Penalty;

@Component
public class PenaltyMapper {
    
    public Penalty toEntity(PenaltyInternalForm penaltyInternalForm) {
        return Penalty.builder()
                .profileId(penaltyInternalForm.getClientId())
                .companyId(penaltyInternalForm.getCompanyId())
                .reason(penaltyInternalForm.getReason())
                .severityLevel(penaltyInternalForm.getSeverityLevel())
                .build();
    }
}
