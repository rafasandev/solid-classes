package com.example.solid_classes.core.log.model;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    private UUID id;
}
