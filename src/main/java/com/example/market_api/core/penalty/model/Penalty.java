package com.example.market_api.core.penalty.model;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.market_api.common.base.AuditableMongoEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "penalties")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Penalty extends AuditableMongoEntity {
    
    private UUID profileId;
    private UUID companyId;
    private int severityLevel;
    private String reason;
}
