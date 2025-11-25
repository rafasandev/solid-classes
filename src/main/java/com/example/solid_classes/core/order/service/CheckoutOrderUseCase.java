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

        // CORREÇÃO: Usar .equals() para comparar UUIDs
        if (!cart.getId().equals(customer.getId())) {
            throw new UserRuleException("Carrinho com os pedidos não pertencem ao usuário atual");
        }

        // CORREÇÃO: Validar se carrinho não está vazio
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new UserRuleException("Carrinho está vazio");
        }

        // CORREÇÃO: Validar estoque de todos os produtos antes de processar
        validateStock(cart.getItems());

        Map<CompanyProfile, List<CartItem>> itemsBySeller = groupItemsBySeller(cart.getItems());

        List<Order> savedOrders = new ArrayList<>();

        for (Map.Entry<CompanyProfile, List<CartItem>> entry : itemsBySeller.entrySet()) {
            CompanyProfile seller = entry.getKey();
            List<CartItem> storeItems = entry.getValue();

            // CORREÇÃO: Calcular total corretamente com BigDecimal imutável
            BigDecimal orderTotal = calculateOrderTotal(storeItems);

            Order order = Order.builder()
                    .customer(cart.getProfile())
                    .company(seller)
                    .pickUpcode(generateUniquePickupCode())
                    .status(OrderStatus.PENDENTE)
                    .orderTotal(orderTotal)
                    .build();

            List<OrderItem> orderItems = storeItems
                    .stream()
                    .map(cartItem -> {
                        // CORREÇÃO: Reservar produto e diminuir estoque
                        cartItem.reserve();
                        cartItem.getProduct().decreaseStock(cartItem.getProductQuantity());
                        return orderItemService.createOrderItemSnapshot(cartItem, order);
                    })
                    .toList();

            order.setOrderItems(orderItems);

            savedOrders.add(orderService.registerOrder(order));
        }
        
        // CORREÇÃO: Limpar carrinho após sucesso
        cartService.clearCart(cart);
        return orderMapper.toResponseDtoList(savedOrders);
    }

    private void validateStock(List<CartItem> items) {
        for (CartItem item : items) {
            if (!item.getProduct().hasStock(item.getProductQuantity())) {
                throw new UserRuleException(
                    String.format("Produto '%s' não possui estoque suficiente. Disponível: %d, Solicitado: %d",
                        item.getProduct().getProductName(),
                        item.getProduct().getStockQuantity(),
                        item.getProductQuantity())
                );
            }
        }
    }

    private Map<CompanyProfile, List<CartItem>> groupItemsBySeller(List<CartItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(item -> item
                        .getProduct()
                        .getCompany()));
    }

    private BigDecimal calculateOrderTotal(List<CartItem> storeItems) {
        // CORREÇÃO: BigDecimal é imutável, precisa atribuir o resultado
        return storeItems.stream()
            .map(CartItem::calculateSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateUniquePickupCode() {
        String CHAR_POOL = "23456789ACDEFGHJKMNPQRSTUVWXYZ";
        int CODE_LENGTH = 5;
        SecureRandom random = new SecureRandom();
        String code;
        int maxAttempts = 10;
        int attempts = 0;

        // CORREÇÃO: Garantir unicidade do código de retirada
        do {
            StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH + 1);
            codeBuilder.append("#");
            
            for (int i = 0; i < CODE_LENGTH; i++) {
                int randomIndex = random.nextInt(CHAR_POOL.length());
                codeBuilder.append(CHAR_POOL.charAt(randomIndex));
            }
            
            code = codeBuilder.toString();
            attempts++;
            
            if (attempts >= maxAttempts) {
                throw new UserRuleException("Não foi possível gerar código único de retirada. Tente novamente.");
            }
        } while (orderService.existsByPickupCode(code));

        return code;
    }
}
