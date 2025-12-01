package com.example.market_api.core.profile.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.cart.service.RegisterCartUseCase;
import com.example.market_api.core.profile.dto.company.CompanyProfileForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.dto.individual.IndividualProfileForm;
import com.example.market_api.core.profile.dto.individual.IndividualProfileResponseDto;
import com.example.market_api.core.profile.mapper.ProfileMapper;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.profile.service.individual.IndividualProfileService;
import com.example.market_api.core.role.model.Role;
import com.example.market_api.core.role.model.enums.RoleName;
import com.example.market_api.core.role.service.RoleService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterProfileUseCase {

    private final IndividualProfileService individualProfileService;
    private final CompanyProfileService companyProfileService;
    private final RoleService roleService;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    private final RegisterCartUseCase registerCartUseCase;

    @Transactional
    public IndividualProfileResponseDto registerIndividual(IndividualProfileForm individualForm) {
        Role individualRole = roleService.getByRoleName(RoleName.INDIVIDUAL);
        User user = userService.signUp(individualForm.getEmail(), individualForm.getPassword(), Set.of(individualRole));

        IndividualProfile newProfile = profileMapper.toEntity(individualForm, user);
        IndividualProfile savedProfile = individualProfileService.save(newProfile);
        
        registerCartUseCase.createCartOnProfileCreation(savedProfile);
        return profileMapper.toResponseDto(savedProfile);
    }

    @Transactional
    public CompanyProfileResponseDto registerCompany(CompanyProfileForm companyForm) {
        Role companyRole = roleService.getByRoleName(RoleName.COMPANY);
        User user = userService.signUp(companyForm.getEmail(), companyForm.getPassword(), Set.of(companyRole));
        
        CompanyProfile newProfile = profileMapper.toEntity(companyForm, user);
        CompanyProfile savedProfile = companyProfileService.save(newProfile);
        
        return profileMapper.toResponseDto(savedProfile);
    }
}
