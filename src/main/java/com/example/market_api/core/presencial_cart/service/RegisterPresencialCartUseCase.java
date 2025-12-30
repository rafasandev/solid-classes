package com.example.market_api.core.presencial_cart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.presencial_cart.dto.PresencialCartForm;
import com.example.market_api.core.presencial_cart.dto.PresencialCartResponseDto;
import com.example.market_api.core.presencial_cart.mapper.PresencialCartMapper;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.profile.service.individual.IndividualProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterPresencialCartUseCase {

    private final PresencialCartService presencialCartService;
    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final IndividualProfileService individualProfileService;

    private final PresencialCartMapper presencialCartMapper;

    @Transactional
    public PresencialCartResponseDto registerPresencialCart(PresencialCartForm presencialCartForm) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        IndividualProfile client = presencialCartForm.getClientCpf() != null
                ? individualProfileService.getByCpf(presencialCartForm.getClientCpf())
                : null;

        PresencialCart presencialCart = presencialCartMapper.toEntity(client, company);
        PresencialCart savedPresencialCart = presencialCartService.save(presencialCart);
        return presencialCartMapper.toResponseDto(savedPresencialCart);
    }
}
