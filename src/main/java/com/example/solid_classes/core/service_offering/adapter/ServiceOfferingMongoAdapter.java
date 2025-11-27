package com.example.solid_classes.core.service_offering.adapter;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedMongoAdapter;
import com.example.solid_classes.core.service_offering.model.mongo.ServiceOfferingDocument;
import com.example.solid_classes.core.service_offering.ports.ServiceCatalogPort;
import com.example.solid_classes.core.service_offering.repository.mongo.ServiceOfferingMongoRepository;

@Component
public class ServiceOfferingMongoAdapter extends NamedMongoAdapter<ServiceOfferingDocument, ServiceOfferingMongoRepository> implements ServiceCatalogPort {

    public ServiceOfferingMongoAdapter(ServiceOfferingMongoRepository repository) {
        super(repository, "Serviço");
    }

    public List<ServiceOfferingDocument> findByCompanyId(UUID companyId) {
        return repository.findByCompanyId(companyId);
    }

    public List<ServiceOfferingDocument> findByCategoryId(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public List<ServiceOfferingDocument> findAvailable() {
        return repository.findByAvailableTrue();
    }

    public List<ServiceOfferingDocument> searchByName(String name) {
        return repository.searchByName(name);
    }

    public boolean existsByServiceName(String serviceName) {
        return repository.existsByServiceName(serviceName);
    }
}
