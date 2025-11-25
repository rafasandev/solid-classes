package com.example.solid_classes.core.order.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
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
import com.example.solid_classes.core.profile.service.individual.IndividualProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutOrderUseCase {

    private final OrderService orderService;
    private final IndividualProfileService customerService;
    private final CartService cartService;
    private final OrderItemService orderItemService;

    private final OrderMapper orderMapper;

    @Transactional
    public List<OrderResponseDto> checkout(OrderCheckoutForm orderCheckoutForm) {
        Cart cart = cartService.getById(orderCheckoutForm.getCartId());
        IndividualProfile customer = customerService.getById(orderCheckoutForm.getCustomerId());

        if (cart.getId() != customer.getId()) {
            throw new UserRuleException("Carrinho com os pedidos não pertencem ao usuário atual");
        }

        Map<CompanyProfile, List<CartItem>> itemsBySeller = groupItemsBySeller(cart.getItems());

        List<Order> savedOrders = new ArrayList<>();

        for (Map.Entry<CompanyProfile, List<CartItem>> entry : itemsBySeller.entrySet()) {
            CompanyProfile seller = entry.getKey();
            List<CartItem> storeItems = entry.getValue();

            BigDecimal orderTotal = calculateOrderTotal(storeItems);

            Order order = Order.builder()
                    .customer(cart.getProfile())
                    .company(seller)
                    .pickUpcode(generateUniqueCode())
                    .status(OrderStatus.PENDENTE)
                    .orderTotal(orderTotal)
                    .build();

            List<OrderItem> orderItems = storeItems
                    .stream()
                    .map(cartItem -> orderItemService.createOrderItemSnapshot(cartItem, order))
                    .toList();

            order.setOrderItems(orderItems);

            savedOrders.add(orderService.registerOrder(order));
        }
        cartService.clearCart(cart);
        return orderMapper.toResponseDtoList(savedOrders);
    }

    private Map<CompanyProfile, List<CartItem>> groupItemsBySeller(List<CartItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(item -> item
                        .getProduct()
                        .getCompany()));
    }

    private BigDecimal calculateOrderTotal(List<CartItem> storeItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : storeItems) {
            total.add(item.calculateSubtotal());
        }

        return total;
    }

    private static String generateUniqueCode() {
        String CHAR_POOL = "23456789ACDEFGHJKMNPQRSTUVWXYZ";
        int CODE_LENGTH = 5;
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        code.append("#");

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_POOL.length());
            code.append(CHAR_POOL.charAt(randomIndex));
        }

        return code.toString();
    }
}
