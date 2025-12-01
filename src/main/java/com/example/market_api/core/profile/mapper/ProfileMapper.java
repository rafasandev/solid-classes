package com.example.market_api.core.profile.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.profile.dto.company.CompanyProfileForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.dto.individual.IndividualProfileForm;
import com.example.market_api.core.profile.dto.individual.IndividualProfileResponseDto;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.user.model.User;

@Component
public class ProfileMapper {
    public CompanyProfile toEntity(CompanyProfileForm profileForm, User user) {

        CompanyProfile companyProfile = CompanyProfile.builder()
                .user(user)
                .companyName(profileForm.getCompanyName())
                .cnpj(profileForm.getCnpj())
                .businessSector(profileForm.getBusinessSector())
                .active(true)
                .build();
        return companyProfile;
    }

    public IndividualProfile toEntity(IndividualProfileForm profileForm, User user) {

        IndividualProfile individualProfile = IndividualProfile.builder()
                .user(user)
                .name(profileForm.getName())
                .cpf(profileForm.getCpf())
                .active(true)
                .build();
        return individualProfile;
    }

    public CompanyProfileResponseDto toResponseDto(CompanyProfile savedProfile) {
        CompanyProfileResponseDto responseDto = CompanyProfileResponseDto.builder()
                .id(savedProfile.getId())
                .companyName(savedProfile.getCompanyName())
                .cnpj(savedProfile.getCnpj())
                .businessSector(savedProfile.getBusinessSector())
                .build();
        return responseDto;
    }

    public IndividualProfileResponseDto toResponseDto(IndividualProfile savedProfile) {
        IndividualProfileResponseDto responseDto = IndividualProfileResponseDto.builder()
                .id(savedProfile.getId())
                .name(savedProfile.getName())
                .cpf(savedProfile.getCpf())
                .build();
        return responseDto;
    }
}
