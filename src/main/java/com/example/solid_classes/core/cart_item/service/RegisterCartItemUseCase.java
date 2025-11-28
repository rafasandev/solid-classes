package com.example.solid_classes.core.cart_item.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.cart.service.CartService;
import com.example.solid_classes.core.cart_item.dto.CartItemForm;
import com.example.solid_classes.core.cart_item.dto.CartItemResponseDto;
import com.example.solid_classes.core.cart_item.mapper.CartItemMapper;
import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.product.model.Product;
import com.example.solid_classes.core.product.service.ProductService;
import com.example.solid_classes.core.product_variation.model.ProductVariation;
import com.example.solid_classes.core.product_variation.service.ProductVariationService;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.service.individual.IndividualProfileService;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterCartItemUseCase {

    private final CartService cartService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final ProductVariationService productVariationService;
    private final IndividualProfileService individualProfileService;
    private final UserService userService;

    private final CartItemMapper cartItemMapper;

    @Transactional
    public CartItemResponseDto registerCartItem(CartItemForm cartItemForm) {
        User loggeduser = userService.getLoggedInUser();
        IndividualProfile client = individualProfileService.getById(loggeduser.getId());
        if (loggeduser == null || client == null || !client.getId().equals(loggeduser.getId())) {
            throw new IllegalArgumentException("Perfil de cliente não encontrado para o usuário logado.");
        }

        Cart cart = cartService.getCartByProfileId(client.getId());
        ProductVariation variation = productVariationService.getById(cartItemForm.getProductVariationId());
        Product product = productService.getById(variation.getProductId());

        productService.validateAvailability(product);
        productVariationService.validateAvailability(variation);

        Optional<CartItem> optCart = cartItemService.getByProductIdAndCartId(variation.getProductId(), cart.getId());

        CartItem newItem;

        // Se o item já existe no carrinho, atualiza a quantidade
        if (optCart.isPresent()) {
            newItem = optCart.get();
            int newQuantity = newItem.getItemQuantity() + cartItemForm.getItemQuantity();
            validateProductAndVariationStock(product, variation, newQuantity);
            newItem.setQuantity(newQuantity);
        } else {
            validateProductAndVariationStock(product, variation, cartItemForm.getItemQuantity());
            newItem = cartItemMapper.toEntity(cartItemForm, product, cart);
            newItem.addQuantity(cartItemForm.getItemQuantity());
        }
        CartItem savedItem = cartItemService.save(newItem);
        return cartItemMapper.toResponseDto(savedItem);
    }

    private void validateProductAndVariationStock(Product product, ProductVariation variation, int requestedQuantity) {
        productService.validateStock(product, requestedQuantity);
        productVariationService.validateStock(variation, requestedQuantity);
    }
}
