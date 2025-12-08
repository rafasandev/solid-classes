package com.example.market_api.core.profile.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.profile.dto.company.CompanyAvailabilityForm;
import com.example.market_api.core.profile.dto.company.CompanyContactConfigurationForm;
import com.example.market_api.core.profile.dto.company.CompanyPaymentMethodsForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.service.company.ConfigureCompanyAvailabilityUseCase;
import com.example.market_api.core.profile.service.company.ConfigureCompanyContactsUseCase;
import com.example.market_api.core.profile.service.company.ConfigureCompanyPaymentMethodsUseCase;
import com.example.market_api.core.profile.service.company.GetCompanyProfileUseCase;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final GetCompanyProfileUseCase getCompanyProfileUseCase;
    private final ConfigureCompanyAvailabilityUseCase configureCompanyAvailabilityUseCase;
    private final ConfigureCompanyContactsUseCase configureCompanyContactsUseCase;
    private final ConfigureCompanyPaymentMethodsUseCase configureCompanyPaymentMethodsUseCase;

    @GetMapping("/company")
    public ResponseEntity<List<CompanyProfileResponseDto>> getAllCompanies() {
        return ResponseEntity.ok(getCompanyProfileUseCase.getAllCompanies());
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<CompanyProfileResponseDto> getCompanyById(@PathVariable UUID id) {
        return ResponseEntity.ok(getCompanyProfileUseCase.getCompanyById(id));
    }

    @GetMapping("/company/cnpj/{cnpj}")
    public ResponseEntity<CompanyProfileResponseDto> getCompanyByCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(getCompanyProfileUseCase.getCompanyByCnpj(cnpj));
    }

    @GetMapping("/company/sector/{businessSector}")
    public ResponseEntity<List<CompanyProfileResponseDto>> getCompaniesByBusinessSector(@PathVariable BusinessSector businessSector) {
        return ResponseEntity.ok(getCompanyProfileUseCase.getCompaniesByBusinessSector(businessSector));
    }

    @PutMapping("/company/{id}/availability")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> configureAvailability(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyAvailabilityForm form) {
        CompanyProfileResponseDto responseDto = configureCompanyAvailabilityUseCase.configureAvailability(id, form);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/company/{id}/contacts")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> configureContacts(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyContactConfigurationForm form) {
        return ResponseEntity.ok(configureCompanyContactsUseCase.execute(id, form));
    }

    @PutMapping("/company/{id}/payment-methods")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> configurePaymentMethods(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyPaymentMethodsForm form) {
        return ResponseEntity.ok(configureCompanyPaymentMethodsUseCase.execute(id, form));
    }
}
