package com.example.solid_classes.core.service_offering.ports;

import java.util.List;
import java.util.UUID;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.service_offering.model.mongo.ServiceOfferingDocument;

/**
 * Port para ServiceOffering no MongoDB.
 */
public interface ServiceCatalogPort extends NamedCrudPort<ServiceOfferingDocument> {
    
    List<ServiceOfferingDocument> findByCompanyId(UUID companyId);
    
    List<ServiceOfferingDocument> findByCategoryId(UUID categoryId);
    
    List<ServiceOfferingDocument> findAvailable();
    
    List<ServiceOfferingDocument> searchByName(String name);
    
    boolean existsByServiceName(String serviceName);
}
