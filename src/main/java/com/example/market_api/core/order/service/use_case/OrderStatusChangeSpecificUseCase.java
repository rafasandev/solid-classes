package com.example.market_api.core.order.service.use_case;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.order.dto.OrderPickupCodeForm;
import com.example.market_api.core.order.dto.OrderResponseDto;
import com.example.market_api.core.order.dto.OrderStatusChangeForm;
import com.example.market_api.core.order.mapper.OrderMapper;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order.model.enums.OrderStatus;
import com.example.market_api.core.penalty.dto.PenaltyInternalForm;
import com.example.market_api.core.penalty.mapper.PenaltyMapper;
import com.example.market_api.core.penalty.model.Penalty;
import com.example.market_api.core.penalty.service.PenaltyService;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStatusChangeSpecificUseCase {

    private final OrderStatusChangeUseCase orderStatusChangeUseCase;
    private final CompanyProfileService companyProfileService;
    private final PenaltyService penaltyService;

    private final OrderMapper orderMapper;
    private final PenaltyMapper penaltyMapper;

    /**
     * Muda o status do pedido de forma genérica, verificando a identidade dos
     * usuários e a validade da transição de status.
     * 
     * @param statusChangeForm
     * @return
     */
    @Transactional
    public OrderResponseDto updateOrderStatusGeneral(OrderStatusChangeForm statusChangeForm) {
        Order updatedOrder = orderStatusChangeUseCase.setOrderStatus(statusChangeForm);
        managePenalties(updatedOrder);
        return orderMapper.toResponseDto(updatedOrder);
    }

    /**
     * Gerencia as penalizações associadas à mudança de status do pedido.
     * 
     * @param updatedOrder
     */
    private void managePenalties(Order updatedOrder) {
        OrderStatus status = updatedOrder.getStatus();

        // Determina a severidade da penalidade com base no status final do pedido.
        Integer severityLevel = resolvePenaltySeverity(status);
        if (severityLevel == null) {
            return;
        }

        PenaltyInternalForm penaltyForm = PenaltyInternalForm.builder()
                .clientId(updatedOrder.getCustomer().getId())
                .companyId(updatedOrder.getCompany().getId())
                .reason(status.name())
                .severityLevel(severityLevel)
                .build();

        Penalty penalty = penaltyMapper.toEntity(penaltyForm);
        penaltyService.save(penalty);
    }

    /**
     * Centraliza a regra de negócio que define quando e com qual severidade
     * uma penalidade deve ser aplicada.
     *
     * @param status
     */
    private Integer resolvePenaltySeverity(OrderStatus status) {
        switch (status) {

            // Cancelamentos em fases sensíveis do fluxo geram penalidade leve,
            // pois impactam a operação, mas ainda permitem recuperação parcial.
            case CANCELADO_PROCESSANDO:
            case CANCELADO_RETIRADA:
                return 1;

            // Pedido expirado indica falha completa no fluxo,
            // gerando penalidade mais severa.
            case EXPIRADO:
                return 2;

            // SEM_RETIRADA gera penalidade própria (nível 1),
            // que pode se somar posteriormente à penalidade por expiração.
            case SEM_RETIRADA:
                return 1;

            // Demais status não geram penalidade.
            default:
                return null;
        }
    }

    /**
     * Muda o status do pedido para COMPLETADO, verificando o código de retirada e
     * atualizando o saldo do vendedor.
     * 
     * @param statusChangeForm
     * @return
     */
    @Transactional
    public OrderResponseDto updateOrderStatusPickup(OrderPickupCodeForm statusChangeForm) {

        // Se for um dos dois status, prossegue com a mudança de status genérica
        Order updatedOrder = orderStatusChangeUseCase.setOrderStatus(statusChangeForm);

        validateOrderStatusForPickup(updatedOrder, statusChangeForm.getNewStatus());
        validatePickupCode(updatedOrder, statusChangeForm.getPickupCode());
        manageSellerBalance(updatedOrder);

        return orderMapper.toResponseDto(updatedOrder);
    }

    /**
     * Valida o código de retirada do pedido.
     * 
     * @param order
     * @param pickupCode
     */
    private void validatePickupCode(Order order, String pickupCode) {
        if (pickupCode == null || !pickupCode.equals(order.getPickUpcode())) {
            throw new BusinessRuleException("O código de retirada informado é inválido.");
        }
    }

    /**
     * Valida se o novo status é compatível com a finalização por retirada.
     * 
     * @param order
     * @param newStatus
     */
    private void validateOrderStatusForPickup(Order order, OrderStatus newStatus) {
        if (newStatus != OrderStatus.COMPLETADO && newStatus != OrderStatus.COMPLETADO_EXPIRADO) {
            throw new BusinessRuleException("O status informado não corresponde ao status de PEDIDO COMPLETADO.");
        }
    }

    /**
     * Gerencia o saldo do vendedor após a finalização do pedido.
     * 
     * @param order
     */
    private void manageSellerBalance(Order order) {
        CompanyProfile seller = order.getCompany();
        BigDecimal orderTotal = order.getOrderTotal();
        BigDecimal sellerBalance = seller.getBalance();

        seller.setBalance(sellerBalance.add(orderTotal));
        companyProfileService.save(seller);
    }
}