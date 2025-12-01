package com.example.market_api.core.service_offering.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedMongoAdapter;
import com.example.market_api.core.service_offering.model.ServiceOffering;
import com.example.market_api.core.service_offering.ports.ServiceOfferingPort;
import com.example.market_api.core.service_offering.repository.mongo.ServiceOfferingRepository;

@Component
public class ServiceOfferingAdapter extends NamedMongoAdapter<ServiceOffering, ServiceOfferingRepository> implements ServiceOfferingPort {

    public ServiceOfferingAdapter(ServiceOfferingRepository repository) {
        super(repository, "Serviço");
    }

    public List<ServiceOffering> findByCompanyId(UUID companyId) {
        return repository.findByCompanyId(companyId);
    }

    public List<ServiceOffering> findByCategoryId(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public List<ServiceOffering> findAvailable() {
        return repository.findByAvailableTrue();
    }

    public List<ServiceOffering> searchByName(String name) {
        return repository.searchByName(name);
    }

    public boolean existsByServiceName(String serviceName) {
        return repository.existsByServiceName(serviceName);
    }
}
