package com.example.market_api.core.penalty.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PenaltyInternalForm {

    private UUID clientId;
    private UUID companyId;
    private String reason;
    private int severityLevel;
}
