package com.example.solid_classes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuração do MongoDB.
 * Habilita auditoria automática para @CreatedDate e @LastModifiedDate.
 * Habilita repositórios MongoDB no pacote especificado.
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.example.solid_classes.core.**.repository.mongo")
public class MongoConfiguration {
    // Auditoria e repositórios MongoDB habilitados via anotações
}
