package com.example.solid_classes.core.service_offering.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.service_offering.model.ServiceOffering;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, UUID>{
    
}
