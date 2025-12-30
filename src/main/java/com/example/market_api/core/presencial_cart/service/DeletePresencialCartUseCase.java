package com.example.market_api.core.presencial_cart.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeletePresencialCartUseCase {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartService presencialCartService;

    @Transactional
    public void deletePresencialCart(UUID cartId) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        PresencialCart cart = presencialCartService.getByIdAndSeller(cartId, company.getId());
        if (cart.isFinalized()) {
            throw new BusinessRuleException("Carrinhos presenciais finalizados não podem ser removidos");
        }

        presencialCartService.delete(cart);
    }
}
