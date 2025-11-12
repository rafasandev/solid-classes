package com.example.solid_classes.core.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.core.profile.dto.CompanyProfileForm;
import com.example.solid_classes.core.profile.dto.IndividualProfileForm;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/individual")
    public ResponseEntity<IndividualProfile> registerIndividualProfile(@RequestBody IndividualProfileForm profileForm) {
        IndividualProfile individualProfile = profileService.registerIndividualProfile(profileForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(individualProfile);
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyProfile> registerCompanyProfile(@RequestBody CompanyProfileForm profileForm) {
        CompanyProfile companyProfile = profileService.registerCompanyProfile(profileForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyProfile);
    }

}
