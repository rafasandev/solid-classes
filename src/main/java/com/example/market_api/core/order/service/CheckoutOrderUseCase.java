package com.example.market_api.core.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.UserRuleException;
import com.example.market_api.core.cart.model.Cart;
import com.example.market_api.core.cart.service.CartService;
import com.example.market_api.core.cart_item.model.CartItem;
import com.example.market_api.core.order.dto.OrderResponseDto;
import com.example.market_api.core.order.mapper.OrderMapper;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order.model.enums.OrderStatus;
import com.example.market_api.core.order_item.model.OrderItem;
import com.example.market_api.core.order_item.service.OrderItemService;
import com.example.market_api.core.product.model.Product;
import com.example.market_api.core.product.service.ProductService;
import com.example.market_api.core.product_variation.model.ProductVariation;
import com.example.market_api.core.product_variation.service.ProductVariationService;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.profile.service.individual.IndividualProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutOrderUseCase {

    private final OrderService orderService;
    private final IndividualProfileService individualProfileService;
    private final CompanyProfileService companyProfileService;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final ProductVariationService productVariationService;
    private final UserService userService;

    private final StockValidator stockValidator;
    private final PickupCodeGenerator pickupCodeGenerator;
    private final OrderCalculator orderCalculator;

    private final OrderMapper orderMapper;

    @Transactional
    public List<OrderResponseDto> checkout() {
        User loggedUser = userService.getLoggedInUser();
        IndividualProfile customer = individualProfileService.getById(loggedUser.getId());
        validateUserIsCustomer(loggedUser, customer);

        Cart cart = cartService.getCartByProfileId(customer.getId());
        validateCartOwnership(cart, customer);
        validateCartNotEmpty(cart);

        stockValidator.validateStock(cart.getItems());

        Map<CompanyProfile, List<CartItem>> itemsBySeller = groupItemsBySeller(cart.getItems());
        List<Order> savedOrders = processOrdersBySeller(cart, itemsBySeller);

        clearCartItems(cart);
        return orderMapper.toResponseDtoList(savedOrders);
    }

    private void validateUserIsCustomer(User user, IndividualProfile customer) {
        if (customer == null || !user.getId().equals(customer.getId())) {
            throw new UserRuleException("Usuário logado não é o cliente do pedido");
        }
    }

    private void validateCartOwnership(Cart cart, IndividualProfile customer) {
        if (cart.getProfile() == null || !cart.getProfile().getId().equals(customer.getId())) {
            throw new UserRuleException("Carrinho não pertence ao usuário atual");
        }
    }

    private void validateCartNotEmpty(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new UserRuleException("Carrinho está vazio");
        }
    }

    private Map<CompanyProfile, List<CartItem>> groupItemsBySeller(List<CartItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(item -> {
                    UUID productId = item.getProductId();
                    Product product = productService.getById(productId);
                    return companyProfileService.getById(product.getCompanyId());
                }));
    }

    private List<Order> processOrdersBySeller(Cart cart, Map<CompanyProfile, List<CartItem>> itemsBySeller) {
        List<Order> savedOrders = new ArrayList<>();

        for (Map.Entry<CompanyProfile, List<CartItem>> entry : itemsBySeller.entrySet()) {
            CompanyProfile seller = entry.getKey();
            List<CartItem> storeItems = entry.getValue();

            BigDecimal orderTotal = orderCalculator.calculateOrderTotal(storeItems);

            Order order = Order.builder()
                    .customer(cart.getProfile())
                    .company(seller)
                    .pickUpcode(pickupCodeGenerator.generateUniqueCode()) // Componente dedicado (SRP)
                    .status(OrderStatus.PENDENTE)
                    .orderTotal(orderTotal)
                    .build();

            List<OrderItem> orderItems = processOrderItems(storeItems, order);
            order.setOrderItems(orderItems);
            savedOrders.add(orderService.save(order));
        }

        return savedOrders;
    }

    private List<OrderItem> processOrderItems(List<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> {
                    cartItem.reserve();
                    Product product = productService.getById(cartItem.getProductId());
                    ProductVariation variation = productVariationService.getById(cartItem.getProductVariationId());

                    // Decrease stock on the product aggregate (embedded variation) and persist
                    product.decreaseVariationStock(variation, cartItem.getItemQuantity());
                    productService.save(product);

                    // Also decrease and persist stock on the standalone ProductVariation document
                    variation.setStockQuantity(variation.getStockQuantity() - cartItem.getItemQuantity());
                    productVariationService.save(variation);

                    return orderItemService.createOrderItemSnapshot(cartItem, order, variation);
                })
                .toList();
    }

    private void clearCartItems(Cart cart) {
        if (cart.getItems() != null) {
            cart.getItems().clear();
            cartService.save(cart);
        }
    }
}
