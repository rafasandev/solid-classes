package com.example.market_api.core.presencial_cart.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.market_api.core.presencial_cart.dto.PresencialCartResponseDto;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

@Component
public class PresencialCartMapper {

    public PresencialCart toEntity(
            IndividualProfile buyer,
            CompanyProfile companyProfile) {
        return PresencialCart.builder()
                .buyer(buyer)
                .seller(companyProfile)
                .buyerName(buyer.getName())
                .buyerDocument(buyer.getCpf())
                .finalized(false)
                .build();
    }

    public PresencialCartResponseDto toResponseDto(PresencialCart presencialCart) {
        return toResponseDto(presencialCart, Collections.emptyList(), BigDecimal.ZERO);
    }

    public PresencialCartResponseDto toResponseDto(
            PresencialCart presencialCart,
            List<PresencialCartItemResponseDto> items,
            BigDecimal cartTotal) {
        return PresencialCartResponseDto.builder()
                .id(presencialCart.getId())
                .companyName(presencialCart.getSeller().getCompanyName())
                .clientName(resolveBuyerName(presencialCart.getBuyer(), presencialCart.getBuyerName()))
                .clientDocument(resolveBuyerDocument(presencialCart))
                .finalized(presencialCart.isFinalized())
                .orderId(presencialCart.getOrder() != null ? presencialCart.getOrder().getId() : null)
                .cartTotal(cartTotal)
                .createdAt(presencialCart.getCreatedAt())
                .finalizedAt(presencialCart.getFinalizedAt())
                .items(items)
                .build();
    }

    private String resolveBuyerName(IndividualProfile buyer, String fallbackName) {
        if (buyer != null) {
            return buyer.getName();
        }
        return fallbackName;
    }

    private String resolveBuyerDocument(PresencialCart presencialCart) {
        if (presencialCart.getBuyer() != null) {
            return presencialCart.getBuyer().getCpf();
        }
        return presencialCart.getBuyerDocument();
    }
}
