package com.example.market_api.core.service_offering.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.core.service_offering.model.ServiceOffering;
import com.example.market_api.core.service_offering.ports.ServiceOfferingPort;

import lombok.RequiredArgsConstructor;

/**
 * Service que encapsula o Port e adiciona validações leves.
 */
@Service
@RequiredArgsConstructor
public class ServiceOfferingService {

    private final ServiceOfferingPort serviceOfferingPort;

    // Métodos CRUD - delegam para o Port
    public ServiceOffering getById(UUID id) {
        return serviceOfferingPort.getById(id);
    }

    public ServiceOffering save(ServiceOffering serviceOffering) {
        return serviceOfferingPort.save(serviceOffering);
    }

    public List<ServiceOffering> findAll() {
        return serviceOfferingPort.findAll();
    }
}
