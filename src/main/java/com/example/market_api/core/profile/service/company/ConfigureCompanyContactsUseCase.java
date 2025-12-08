package com.example.market_api.core.profile.service.company;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.profile.dto.company.CompanyContactConfigurationForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.mapper.ProfileMapper;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.contact_info.service.ContactInfoService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfigureCompanyContactsUseCase {

    private final CompanyProfileService companyProfileService;
    private final ContactInfoService contactInfoService;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    @Transactional
    public CompanyProfileResponseDto execute(UUID companyId, CompanyContactConfigurationForm form) {
        CompanyProfile company = companyProfileService.getById(companyId);
        User loggedUser = userService.getLoggedInUser();
        validateCompanyOwnership(company, loggedUser);

        contactInfoService.replaceContacts(company.getUser(), form.getContacts());

        CompanyProfile refreshedCompany = companyProfileService.getById(companyId);
        return profileMapper.toResponseDto(refreshedCompany);
    }

    private void validateCompanyOwnership(CompanyProfile company, User user) {
        if (company == null || user == null || !company.getId().equals(user.getId())) {
            throw new BusinessRuleException("Usuário logado não pode alterar os contatos desta empresa");
        }
    }
}
