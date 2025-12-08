package com.example.market_api.core.profile.service.company;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.payment_method.model.PaymentMethod;
import com.example.market_api.core.payment_method.service.PaymentMethodService;
import com.example.market_api.core.profile.dto.company.CompanyPaymentMethodsForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.mapper.ProfileMapper;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfigureCompanyPaymentMethodsUseCase {

    private final CompanyProfileService companyProfileService;
    private final PaymentMethodService paymentMethodService;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    @Transactional
    public CompanyProfileResponseDto execute(UUID companyId, CompanyPaymentMethodsForm form) {
        CompanyProfile company = companyProfileService.getById(companyId);
        User loggedUser = userService.getLoggedInUser();
        validateCompanyOwnership(company, loggedUser);

        Set<PaymentMethod> paymentMethods = paymentMethodService.getAllByIds(form.getPaymentMethodIds());
        replacePaymentMethods(company, paymentMethods);

        CompanyProfile saved = companyProfileService.save(company);
        return profileMapper.toResponseDto(saved);
    }

    private void replacePaymentMethods(CompanyProfile company, Set<PaymentMethod> newMethods) {
        if (company.getPaymentMethods() != null && !company.getPaymentMethods().isEmpty()) {
            List.copyOf(company.getPaymentMethods()).forEach(company::removePaymentMethod);
        }
        newMethods.forEach(company::addPaymentMethod);
    }

    private void validateCompanyOwnership(CompanyProfile company, User user) {
        if (company == null || user == null || !company.getId().equals(user.getId())) {
            throw new BusinessRuleException("Usuário logado não pode alterar os pagamentos desta empresa");
        }
    }
}
