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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterCartItemUseCase {

    private final CartItemService cartItemService;
    private final ProductService productService;
    private final CartService cartService;

    private final CartItemMapper cartItemMapper;

    @Transactional
    public CartItemResponseDto registerCartItem(CartItemForm cartItemForm) {
        Cart cart = cartService.getByProfileId(cartItemForm.getUserId());
        Product product = productService.getById(cartItemForm.getProductId());

        Optional<CartItem> optCart = cartItemService.getByProductIdAndCartId(cartItemForm.getProductId(), cart.getId());
        CartItem newItem;

        if (optCart.isPresent()) {
            newItem = optCart.get();
            newItem.addItemCart(cartItemForm.getItemQuantity());
        } else {
            newItem = cartItemMapper.toEntity(cartItemForm, product, cart);
            newItem.initCartItems();
        }

        CartItem savedItem = cartItemService.createCartItem(newItem);
        CartItemResponseDto itemResponse = cartItemMapper.toResponseDto(savedItem);
        return itemResponse;
    }
}
