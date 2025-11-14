package com.example.solid_classes.core.service_offering.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.service_offering.dto.ServiceOfferingForm;
import com.example.solid_classes.core.service_offering.model.ServiceOffering;
import com.example.solid_classes.core.service_offering.service.ServiceOfferingService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;
    
    @PostMapping("/")
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceOfferingForm serviceForm) {
        ServiceOffering service = serviceOfferingService.createService(serviceForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }
    
}
