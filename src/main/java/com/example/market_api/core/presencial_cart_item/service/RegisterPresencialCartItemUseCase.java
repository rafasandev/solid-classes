package com.example.market_api.core.presencial_cart_item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
import com.example.market_api.core.presencial_cart.service.PresencialCartService;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemForm;
import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;
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
public class RegisterPresencialCartItemUseCase {

    private final PresencialCartItemService presencialCartItemService;
    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartService presencialCartService;
    private final ProductVariationService productVariationService;
    private final ProductService productService;

    private final PresencialCartItemMapper presencialCartItemMapper;

    @Transactional
    public PresencialCartItemResponseDto registerPresencialCartItem(PresencialCartItemForm presencialCartItemForm) {
        User userLogged = userService.getLoggedInUser();
        CompanyProfile company = companyProfileService.getById(userLogged.getId());
        companyProfileService.validateIsActive(company);

        PresencialCart presencialCart = presencialCartService
                .getByIdAndSeller(presencialCartItemForm.getPresencialCartId(), company.getId());
        validateCartOpen(presencialCart);

        ProductVariation productVariation = productVariationService
                .getById(presencialCartItemForm.getProductVariationId());
        Product product = productService.getById(productVariation.getProductId());

        validateOwnership(product, company);
        validateProductAndVariationStock(product, productVariation, presencialCartItemForm.getItemQuantity());

        PresencialCartItem presencialCartItem = presencialCartItemService
                .findByCartAndVariation(presencialCart.getId(), productVariation.getId())
                .orElse(null);

        if (presencialCartItem == null) {
            presencialCartItem = presencialCartItemMapper.toEntity(
                    presencialCart,
                    product,
                    productVariation,
                    presencialCartItemForm.getItemQuantity());
        } else {
            presencialCartItem.setQuantity(presencialCartItemForm.getItemQuantity());
        }

        PresencialCartItem savedItem = presencialCartItemService.save(presencialCartItem);
        presencialCart.addItem(savedItem);

        return presencialCartItemMapper.toResponseDto(savedItem, product, productVariation);
    }

    private void validateCartOpen(PresencialCart presencialCart) {
        if (presencialCart.isFinalized()) {
            throw new BusinessRuleException("Carrinhos finalizados não aceitam alterações");
        }
    }

    private void validateOwnership(Product product, CompanyProfile company) {
        if (!product.getCompanyId().equals(company.getId())) {
            throw new BusinessRuleException("Produto não pertence à empresa autenticada");
        }
    }

    private void validateProductAndVariationStock(Product product, ProductVariation variation, int requestedQuantity) {
        productService.validateAvailability(product);
        productVariationService.validateAvailability(variation);
        productService.validateStock(product, requestedQuantity);
        productVariationService.validateStock(variation, requestedQuantity);
    }
}
