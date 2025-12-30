package com.example.market_api.core.presencial_cart_item.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemUpdateForm;
import com.example.market_api.core.presencial_cart_item.mapper.PresencialCartItemMapper;
import com.example.market_api.core.presencial_cart_item.model.PresencialCartItem;
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
public class UpdatePresencialCartItemUseCase {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartItemService presencialCartItemService;
    private final ProductService productService;
    private final ProductVariationService productVariationService;
    private final PresencialCartItemMapper presencialCartItemMapper;

    @Transactional
    public PresencialCartItemResponseDto updatePresencialCartItem(UUID itemId, PresencialCartItemUpdateForm updateForm) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(company);

        PresencialCartItem item = presencialCartItemService.getById(itemId);
        validateOwnership(item, company);
        validateCartOpen(item);

        Product product = productService.getById(item.getProductId());
        ProductVariation variation = productVariationService.getById(item.getProductVariationId());

        validateStock(product, variation, updateForm.getItemQuantity());

        item.setQuantity(updateForm.getItemQuantity());

        PresencialCartItem savedItem = presencialCartItemService.save(item);
        return presencialCartItemMapper.toResponseDto(savedItem, product, variation);
    }

    private void validateOwnership(PresencialCartItem item, CompanyProfile company) {
        if (!item.getPresencialCart().getSeller().getId().equals(company.getId())) {
            throw new BusinessRuleException("Item não pertence à empresa autenticada");
        }
    }

    private void validateCartOpen(PresencialCartItem item) {
        if (item.getPresencialCart().isFinalized()) {
            throw new BusinessRuleException("Carrinhos finalizados não aceitam alterações");
        }
    }

    private void validateStock(Product product, ProductVariation variation, int quantity) {
        productService.validateAvailability(product);
        productVariationService.validateAvailability(variation);
        productService.validateStock(product, quantity);
        productVariationService.validateStock(variation, quantity);
    }
}
