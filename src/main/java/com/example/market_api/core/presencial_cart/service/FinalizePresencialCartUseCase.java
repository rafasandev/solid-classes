package com.example.market_api.core.presencial_cart.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.order.dto.OrderResponseDto;
import com.example.market_api.core.order.mapper.OrderMapper;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order.model.enums.OrderStatus;
import com.example.market_api.core.order.service.OrderService;
import com.example.market_api.core.order.service.support.PickupCodeGenerator;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.order_item.service.OrderItemService;
import com.example.market_api.core.presencial_cart.model.PresencialCart;
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
public class FinalizePresencialCartUseCase {

    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final PresencialCartService presencialCartService;
    private final ProductService productService;
    private final ProductVariationService productVariationService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final OrderMapper orderMapper;
    private final PickupCodeGenerator pickupCodeGenerator;

    @Transactional
    public OrderResponseDto finalizePresencialCart(UUID cartId) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile seller = companyProfileService.getById(loggedUser.getId());
        companyProfileService.validateIsActive(seller);

        PresencialCart cart = presencialCartService.getByIdAndSeller(cartId, seller.getId());
        ensureCartIsOpen(cart);
        ensureCartHasItems(cart);

        Order order = buildOrder(cart);
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> processOrderItem(item, seller, order))
                .toList();

        order.setOrderItems(orderItems);
        order.setOrderTotal(orderService.calculateOrderTotal(orderItems));

        Order savedOrder = orderService.save(order);
        cart.markAsFinalized(savedOrder);
        presencialCartService.save(cart);

        creditSellerBalance(seller, savedOrder.getOrderTotal());

        return orderMapper.toResponseDto(savedOrder);
    }

    private void ensureCartIsOpen(PresencialCart cart) {
        if (cart.isFinalized()) {
            throw new BusinessRuleException("Carrinho presencial já finalizado");
        }
    }

    private void ensureCartHasItems(PresencialCart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessRuleException("Adicione ao menos um item antes de finalizar a venda presencial");
        }
    }

    private Order buildOrder(PresencialCart cart) {
        return Order.builder()
                .pickUpcode(pickupCodeGenerator.generateUniqueCode())
                .status(OrderStatus.FINALIZADO_PRESENCIAL)
                .isPaid(true)
                .paidAt(LocalDateTime.now())
                .customer(cart.getBuyer())
                .company(cart.getSeller())
                .build();
    }

    private OrderItem processOrderItem(PresencialCartItem item, CompanyProfile seller, Order order) {
        Product product = productService.getById(item.getProductId());
        ProductVariation variation = productVariationService.getById(item.getProductVariationId());

        validateOwnership(product, seller);
        validateStock(product, variation, item.getItemQuantity());

        variation.decreaseVariationStock(item.getItemQuantity());
        productVariationService.save(variation);
        productService.save(product);

        return orderItemService.createOrderItemSnapshot(item, order);
    }

    private void validateOwnership(Product product, CompanyProfile seller) {
        if (!product.getCompanyId().equals(seller.getId())) {
            throw new BusinessRuleException("Produto não pertence à empresa autenticada");
        }
    }

    private void validateStock(Product product, ProductVariation variation, int quantity) {
        productService.validateAvailability(product);
        productVariationService.validateAvailability(variation);
        productService.validateStock(product, quantity);
        productVariationService.validateStock(variation, quantity);
    }

    private void creditSellerBalance(CompanyProfile seller, BigDecimal amount) {
        seller.setBalance(seller.getBalance().add(amount));
        companyProfileService.save(seller);
    }
}
