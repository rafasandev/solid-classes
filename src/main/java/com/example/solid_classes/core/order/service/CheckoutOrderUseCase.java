package com.example.solid_classes.core.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solid_classes.common.exception.UserRuleException;
import com.example.solid_classes.core.cart.model.Cart;
import com.example.solid_classes.core.cart.service.CartService;
import com.example.solid_classes.core.cart_item.model.CartItem;
import com.example.solid_classes.core.order.dto.OrderCheckoutForm;
import com.example.solid_classes.core.order.dto.OrderResponseDto;
import com.example.solid_classes.core.order.mapper.OrderMapper;
import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.order.model.enums.OrderStatus;
import com.example.solid_classes.core.order_item.model.OrderItem;
import com.example.solid_classes.core.order_item.service.OrderItemService;
import com.example.solid_classes.core.profile.model.company.CompanyProfile;
import com.example.solid_classes.core.profile.model.individual.IndividualProfile;
import com.example.solid_classes.core.profile.service.company.CompanyProfileService;
import com.example.solid_classes.core.profile.service.individual.IndividualProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutOrderUseCase {

    private final OrderService orderService;
    private final IndividualProfileService individualProfileService;
    private final CompanyProfileService companyProfileService;
    private final CartService cartService;
    private final OrderItemService orderItemService;

    private final StockValidator stockValidator;
    private final PickupCodeGenerator pickupCodeGenerator;
    private final OrderCalculator orderCalculator;

    private final OrderMapper orderMapper;

    @Transactional
    public List<OrderResponseDto> checkout(OrderCheckoutForm orderCheckoutForm) {
        Cart cart = cartService.getById(orderCheckoutForm.getCartId());
        IndividualProfile customer = individualProfileService.getById(orderCheckoutForm.getCustomerId());

        validateCartOwnership(cart, customer);
        validateCartNotEmpty(cart);

        stockValidator.validateStock(cart.getItems());

        Map<CompanyProfile, List<CartItem>> itemsBySeller = groupItemsBySeller(cart.getItems());
        List<Order> savedOrders = processOrdersBySeller(cart, itemsBySeller);
        clearCartItems(cart);
        return orderMapper.toResponseDtoList(savedOrders);
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
        // CompanyProfile company = companyProfileService.getById(null)
        return items.stream()
                .collect(Collectors.groupingBy(item -> companyProfileService.getById(item.getProduct().getCompanyId())));
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
                    cartItem.getProduct().decreaseStock(cartItem.getProductQuantity());

                    return createOrderItemSnapshot(cartItem, order);
                })
                .toList();
    }

    private OrderItem createOrderItemSnapshot(CartItem cartItem, Order order) {
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(cartItem.getProduct())
                .productName(cartItem.getProduct().getProductName())
                .productPrice(cartItem.getUnitPriceSnapshot())
                .productQuantity(cartItem.getProductQuantity())
                .subtotal(cartItem.calculateSubtotal())
                .build();

        return orderItemService.save(orderItem);
    }

    private void clearCartItems(Cart cart) {
        if (cart.getItems() != null) {
            cart.getItems().clear();
            cartService.save(cart);
        }
    }
}
