package com.example.solid_classes.core.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.profile.dto.CompanyProfileForm;
import com.example.solid_classes.core.profile.dto.CompanyProfileResponseDto;
import com.example.solid_classes.core.profile.dto.IndividualProfileForm;
import com.example.solid_classes.core.profile.dto.IndividualProfileResponseDto;
import com.example.solid_classes.core.profile.mapper.ProfileMapper;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.service.company.CompanyProfileService;
import com.example.solid_classes.core.profile.service.individual.IndividualProfileService;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProfileUseCase {

    private final UserService userService;
    private final IndividualProfileService individualProfileService;
    private final CompanyProfileService companyProfileService;
    private final ProfileMapper profileMapper;

    @Transactional
    public CompanyProfileResponseDto registerCompany(CompanyProfileForm companyForm) {
        User user = userService.signUp(companyForm.getEmail(), companyForm.getPassword());
        CompanyProfile newProfile = profileMapper.toEntity(companyForm, user);
        CompanyProfile savedProfile = companyProfileService.registerProfile(newProfile);
        CompanyProfileResponseDto responseDto = profileMapper.toResponseDto(savedProfile);
        return responseDto;
    }

    @Transactional
    public IndividualProfileResponseDto registerIndividual(IndividualProfileForm individualForm) {
        User user = userService.signUp(individualForm.getEmail(), individualForm.getPassword());
        IndividualProfile newProfile = profileMapper.toEntity(individualForm, user);
        IndividualProfile savedProfile = individualProfileService.registerProfile(newProfile);
        IndividualProfileResponseDto responseDto = profileMapper.toResponseDto(savedProfile);
        return responseDto;
    }

}
