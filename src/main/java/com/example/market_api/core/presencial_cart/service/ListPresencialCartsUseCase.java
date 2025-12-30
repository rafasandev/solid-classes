package com.example.market_api.core.presencial_cart.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ListPresencialCartsUseCase {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartService presencialCartService;
    private final ProductService productService;
    private final ProductVariationService productVariationService;
    private final PresencialCartItemMapper presencialCartItemMapper;
    private final PresencialCartMapper presencialCartMapper;

    @Transactional(readOnly = true)
    public Page<PresencialCartResponseDto> getListPresencialCart(Pageable pageable) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        Page<PresencialCart> carts = presencialCartService.findBySeller(company.getId(), pageable);
        return carts.map(this::mapCartToResponse);
    }

    private PresencialCartResponseDto mapCartToResponse(PresencialCart cart) {
        List<PresencialCartItemResponseDto> items = cart.getItems() == null
                ? Collections.emptyList()
                : cart.getItems().stream()
                        .map(this::mapItemToResponse)
                        .toList();

        BigDecimal cartTotal = items.stream()
                .map(PresencialCartItemResponseDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return presencialCartMapper.toResponseDto(cart, items, cartTotal);
    }

    private PresencialCartItemResponseDto mapItemToResponse(PresencialCartItem item) {
        Product product = productService.getById(item.getProductId());
        ProductVariation variation = productVariationService.getById(item.getProductVariationId());
        return presencialCartItemMapper.toResponseDto(item, product, variation);
    }
}
