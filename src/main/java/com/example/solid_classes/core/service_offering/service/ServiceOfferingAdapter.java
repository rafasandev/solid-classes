package com.example.solid_classes.core.service_offering.service;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;
import com.example.solid_classes.core.service_offering.ports.ServiceOfferingPort;
import com.example.solid_classes.core.service_offering.repository.ServiceOfferingRepository;

@Component
public class ServiceOfferingAdapter extends NamedCrudAdapter<ServiceOffering, ServiceOfferingRepository> implements ServiceOfferingPort{
    
    public ServiceOfferingAdapter(ServiceOfferingRepository serviceOfferingRepository) {
        super(serviceOfferingRepository, "Serviço");
    }
}
