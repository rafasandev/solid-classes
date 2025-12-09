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

import com.example.market_api.core.profile.dto.common.ProfileContactConfigurationForm;
import com.example.market_api.core.profile.dto.company.CompanyAvailabilityForm;
import com.example.market_api.core.profile.dto.company.CompanyPaymentMethodsForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.dto.individual.IndividualProfileResponseDto;
import com.example.market_api.core.profile.model.company.enums.BusinessSector;
import com.example.market_api.core.profile.service.ConfigureProfileContactsUseCase;
import com.example.market_api.core.profile.service.company.use_case.ConfigureCompanyAvailabilityUseCase;
import com.example.market_api.core.profile.service.company.use_case.ConfigureCompanyPaymentMethodsUseCase;
import com.example.market_api.core.profile.service.company.use_case.GetCompanyProfileUseCase;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final GetCompanyProfileUseCase getCompanyProfileUseCase;
    private final ConfigureCompanyAvailabilityUseCase configureCompanyAvailabilityUseCase;
    private final ConfigureProfileContactsUseCase configureProfileContactsUseCase;
    private final ConfigureCompanyPaymentMethodsUseCase configureCompanyPaymentMethodsUseCase;

    // ---------------------------------- Individual Profile Endpoints ------------------------------------- //

    @PutMapping("/individual/{id}/contacts")
    @PreAuthorize("hasRole('INDIVIDUAL')")
    public ResponseEntity<IndividualProfileResponseDto> configureIndividualContacts(
            @PathVariable UUID id,
            @Valid @RequestBody ProfileContactConfigurationForm form) {
        return ResponseEntity.ok(configureProfileContactsUseCase.configureIndividualContacts(id, form));
    }

    // ------------------------------------- Company Profile Endpoints ------------------------------------- //
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
    public ResponseEntity<CompanyProfileResponseDto> configureCompanyContacts(
            @PathVariable UUID id,
            @Valid @RequestBody ProfileContactConfigurationForm form) {
        return ResponseEntity.ok(configureProfileContactsUseCase.configureCompanyContacts(id, form));
    }

    @PutMapping("/company/{id}/payment-methods")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> configurePaymentMethods(
            @PathVariable UUID companyId,
            @Valid @RequestBody CompanyPaymentMethodsForm form) {
        return ResponseEntity.ok(configureCompanyPaymentMethodsUseCase.configurePaymentMethods(companyId, form));
    }
}
