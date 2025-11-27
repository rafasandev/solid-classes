package com.example.solid_classes.core.service_offering.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.solid_classes.core.service_offering.model.mongo.ServiceOfferingDocument;

/**
 * Repository MongoDB para ServiceOffering.
 * Fornece acesso ao catálogo de serviços com consultas otimizadas.
 */
@Repository
public interface ServiceOfferingMongoRepository extends MongoRepository<ServiceOfferingDocument, UUID> {

    List<ServiceOfferingDocument> findByCompanyId(UUID companyId);

    List<ServiceOfferingDocument> findByCategoryId(UUID categoryId);

    List<ServiceOfferingDocument> findByAvailableTrue();

    @Query("{'serviceName': {$regex: ?0, $options: 'i'}}")
    List<ServiceOfferingDocument> searchByName(String name);

    boolean existsByServiceName(String serviceName);
}
