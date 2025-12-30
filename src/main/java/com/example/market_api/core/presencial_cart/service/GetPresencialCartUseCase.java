package com.example.market_api.core.presencial_cart.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.presencial_cart.dto.PresencialCartResponseDto;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
import com.example.market_api.core.presencial_cart_item.mapper.PresencialCartItemMapper;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
import com.example.market_api.core.presencial_cart.mapper.PresencialCartMapper;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.service.ProductService;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.product_variation.service.ProductVariationService;
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
    private final ProductService productService;
    private final ProductVariationService productVariationService;
    private final PresencialCartItemMapper presencialCartItemMapper;
    private final PresencialCartMapper presencialCartMapper;

    @Transactional(readOnly = true)
    public PresencialCartResponseDto getPresencialCart(UUID cartId) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        PresencialCart cart = presencialCartService.getByIdAndSeller(cartId, company.getId());
        List<PresencialCartItemResponseDto> items = buildItemResponses(cart);
        BigDecimal cartTotal = calculateCartTotal(items);

        return presencialCartMapper.toResponseDto(cart, items, cartTotal);
    }

    private List<PresencialCartItemResponseDto> buildItemResponses(PresencialCart cart) {
        if (cart.getItems() == null) {
            return Collections.emptyList();
        }

        return cart.getItems().stream()
                .map(this::mapItemToResponse)
                .toList();
    }

    private PresencialCartItemResponseDto mapItemToResponse(PresencialCartItem item) {
        Product product = productService.getById(item.getProductId());
        ProductVariation variation = productVariationService.getById(item.getProductVariationId());
        return presencialCartItemMapper.toResponseDto(item, product, variation);
    }

    private BigDecimal calculateCartTotal(List<PresencialCartItemResponseDto> items) {
        return items.stream()
                .map(PresencialCartItemResponseDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
