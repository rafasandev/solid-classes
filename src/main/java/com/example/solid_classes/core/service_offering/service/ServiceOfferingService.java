package com.example.solid_classes.core.service_offering.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.service_offering.model.ServiceOffering;
import com.example.solid_classes.core.service_offering.ports.ServiceOfferingPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService {

    private final ServiceOfferingPort serviceOfferingPort;

    @Transactional
    public ServiceOffering createService(ServiceOffering newService) {
        return serviceOfferingPort.save(newService);
    }
}
