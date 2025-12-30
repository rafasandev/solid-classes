package com.example.market_api.core.presencial_cart_item.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
import com.example.market_api.core.presencial_cart_item.mapper.PresencialCartItemMapper;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetPresencialCartItemUseCase {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartItemService presencialCartItemService;
    private final PresencialCartItemMapper presencialCartItemMapper;

    @Transactional(readOnly = true)
    public PresencialCartItemResponseDto getPresencialCartItem(UUID itemId) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        PresencialCartItem item = presencialCartItemService.getById(itemId);
        validateOwnership(item, company);

        return presencialCartItemMapper.toResponseDto(item);
    }

    private void validateOwnership(PresencialCartItem item, CompanyProfile company) {
        if (!item.getPresencialCart().getSeller().getId().equals(company.getId())) {
            throw new BusinessRuleException("Item não pertence à empresa autenticada");
        }
    }
}
