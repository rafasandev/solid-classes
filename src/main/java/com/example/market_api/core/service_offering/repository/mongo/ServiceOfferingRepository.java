package com.example.market_api.core.service_offering.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.market_api.core.service_offering.model.ServiceOffering;

/**
 * Repository MongoDB para ServiceOffering.
 * Fornece acesso ao catálogo de serviços com consultas otimizadas.
 */
@Repository
public interface ServiceOfferingRepository extends MongoRepository<ServiceOffering, UUID> {

    List<ServiceOffering> findByCompanyId(UUID companyId);

    List<ServiceOffering> findByCategoryId(UUID categoryId);

    List<ServiceOffering> findByAvailableTrue();

    @Query("{'serviceName': {$regex: ?0, $options: 'i'}}")
    List<ServiceOffering> searchByName(String name);

    boolean existsByServiceName(String serviceName);
}
