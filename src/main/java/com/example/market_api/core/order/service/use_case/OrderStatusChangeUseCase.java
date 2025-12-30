package com.example.market_api.core.order.service.use_case;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.order.dto.OrderStatusChangeForm;
import com.example.market_api.core.order.model.Order;
import com.example.market_api.core.order.model.enums.OrderStatus;
import com.example.market_api.core.order.service.OrderService;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.profile.service.individual.IndividualProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStatusChangeUseCase {

    // Service de verificação e recuperação dos status de pedido de acordo com o
    // status atual
    private final OrderService orderService;
    private final UserService userService;
    private final CompanyProfileService companyProfileService;
    private final IndividualProfileService individualProfileService;

    /**
     * Muda o status do pedido de forma genérica, verificando a identidade dos
     * usuários e a validade da transição de status.
     * 
     * @param statusChangeForm
     * @return
     */
    @Transactional
    public Order setOrderStatus(OrderStatusChangeForm statusChangeForm) {
        User loggedUser = userService.getLoggedInUser();
        CompanyProfile seller = companyProfileService.getById(loggedUser.getId());
        IndividualProfile client = individualProfileService.getById(statusChangeForm.getClientId());
        Order order = orderService.getById(statusChangeForm.getOrderId());

        if (client.getId() != order.getCustomer().getId() || seller.getId() != order.getCompany().getId()) {
            throw new IllegalArgumentException("Usuário não autorizado a alterar o status deste pedido.");
        }

        List<OrderStatus> allowedStatuses = getAllowedStatusByActualStatus(order);
        if (!allowedStatuses.contains(statusChangeForm.getNewStatus())) {
            throw new BusinessRuleException("Transição de status inválida.");
        }
        order.setStatus(statusChangeForm.getNewStatus());
        return order;
    }

    /**
     * Recupera a lista de status permitidos com base no status atual do pedido.
     * @param currentOrder
     * @return
     */
    @Transactional
    public List<OrderStatus> getAllowedStatusByActualStatus(Order currentOrder) {
        List<OrderStatus> allowedStatuses = List.of();
        OrderStatus currentStatus = currentOrder.getStatus();
        switch (currentStatus) {
            case PENDENTE:
                allowedStatuses = List.of(OrderStatus.PROCESSANDO, OrderStatus.PEDIDO_CANCELADO);
                break;
            case PEDIDO_CANCELADO:
                break;
            case PROCESSANDO:
                allowedStatuses = List.of(OrderStatus.PRONTO_RETIRADA, OrderStatus.CANCELADO_PROCESSANDO);
                break;
            case CANCELADO_PROCESSANDO:
                break;
            case PRONTO_RETIRADA:
                allowedStatuses = List.of(OrderStatus.COMPLETADO, OrderStatus.EXPIRADO, OrderStatus.CANCELADO_RETIRADA);
                break;
            case CANCELADO_RETIRADA:
                break;
            case COMPLETADO:
                break;
            case EXPIRADO:
                allowedStatuses = List.of(OrderStatus.SEM_RETIRADA, OrderStatus.COMPLETADO_EXPIRADO);
                break;
            case SEM_RETIRADA:
                break;
            case COMPLETADO_EXPIRADO:
                break;
            case FINALIZADO_PRESENCIAL:
                break;
            default:
                throw new BusinessRuleException("Status de pedido inválido");
        }
        return allowedStatuses;
    }
}
