package com.example.solid_classes.core.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.profile.dto.CompanyProfileForm;
import com.example.solid_classes.core.profile.dto.IndividualProfileForm;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.repository.CompanyProfileRepository;
import com.example.solid_classes.core.profile.repository.IndividualProfileRepository;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;
    private final IndividualProfileRepository individualProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;

    @Transactional
    public IndividualProfile registerIndividualProfile(IndividualProfileForm profileForm) {

        User user = userService.signUp(profileForm.getEmail(), profileForm.getPassword());

        IndividualProfile newProfile = IndividualProfile.builder()
            .user(user)
            .cpf(profileForm.getCpf())
            .name(profileForm.getName())
            .build();

        return individualProfileRepository.save(newProfile);
    }

    @Transactional
    public CompanyProfile registerCompanyProfile(CompanyProfileForm profileForm) {
        User user = userService.signUp(profileForm.getEmail(), profileForm.getPassword());

        CompanyProfile newProfile = CompanyProfile.builder()
                .user(user)
                .cnpj(profileForm.getCnpj())
                .companyName(profileForm.getCompanyName())
                .build();

        return companyProfileRepository.save(newProfile);
    }



}
