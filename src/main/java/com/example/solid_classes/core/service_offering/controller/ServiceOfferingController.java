package com.example.solid_classes.core.service_offering.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.service_offering.dto.ServiceOfferingForm;
import com.example.solid_classes.core.service_offering.dto.ServiceOfferingResponseDto;
import com.example.solid_classes.core.service_offering.service.RegisterServiceOfferingUseCase;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final RegisterServiceOfferingUseCase serviceOfferingService;

    
    @PostMapping("")
    public ResponseEntity<ServiceOfferingResponseDto> createService(@RequestBody ServiceOfferingForm serviceForm) {
        ServiceOfferingResponseDto service = serviceOfferingService.registerServiceOffering(serviceForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }
    
}
