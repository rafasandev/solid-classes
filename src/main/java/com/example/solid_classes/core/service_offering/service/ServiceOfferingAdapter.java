package com.example.solid_classes.core.service_offering.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.classes.NamedCrudAdapter;
import com.example.solid_classes.core.service_offering.interfaces.ServiceOfferingPort;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;
import com.example.solid_classes.core.service_offering.repository.ServiceOfferingRepository;

@Service
public class ServiceOfferingAdapter extends NamedCrudAdapter<ServiceOffering, ServiceOfferingRepository> implements ServiceOfferingPort{
    
    public ServiceOfferingAdapter(ServiceOfferingRepository serviceOfferingRepository) {
        super(serviceOfferingRepository, "Serviço");
    }
}
