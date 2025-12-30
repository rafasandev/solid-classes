package com.example.market_api.core.presencial_cart.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.presencial_cart.dto.PresencialCartResponseDto;
import com.example.market_api.core.presencial_cart.mapper.PresencialCartMapper;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetPresencialCartUseCase {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartService presencialCartService;
    private final PresencialCartMapper presencialCartMapper;

    @Transactional(readOnly = true)
    public PresencialCartResponseDto getPresencialCart(UUID cartId) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        PresencialCart cart = presencialCartService.getByIdAndSeller(cartId, company.getId());
        return presencialCartMapper.toResponseDto(cart);
    }
}
