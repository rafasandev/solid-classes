package com.example.solid_classes.core.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.profile.dto.CompanyProfileForm;
import com.example.solid_classes.core.profile.dto.CompanyProfileResponseDto;
import com.example.solid_classes.core.profile.dto.IndividualProfileForm;
import com.example.solid_classes.core.profile.dto.IndividualProfileResponseDto;
import com.example.solid_classes.core.profile.service.RegisterProfileUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final RegisterProfileUseCase registerProfileUseCase;

    // Endpoint para registro de perfil individual
    @PostMapping("/individual")
    public ResponseEntity<IndividualProfileResponseDto> registerIndividualProfile(@RequestBody IndividualProfileForm profileForm) {
        IndividualProfileResponseDto responseDto = registerProfileUseCase.registerIndividual(profileForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Endpoint para registro de perfil de empresa
    @PostMapping("/company")
    public ResponseEntity<CompanyProfileResponseDto> registerCompanyProfile(@RequestBody CompanyProfileForm profileForm) {
        CompanyProfileResponseDto responseDto = registerProfileUseCase.registerCompany(profileForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
