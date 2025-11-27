package com.example.solid_classes.core.service_offering.ports;

import java.util.List;
import java.util.UUID;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;

public interface ServiceOfferingPort extends NamedCrudPort<ServiceOffering> {
    
    List<ServiceOffering> findByCompanyId(UUID companyId);
    
    List<ServiceOffering> findByCategoryId(UUID categoryId);
    
    List<ServiceOffering> findAvailable();
    
    List<ServiceOffering> searchByName(String name);
    
    boolean existsByServiceName(String serviceName);
}
