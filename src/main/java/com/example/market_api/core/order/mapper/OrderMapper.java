package com.example.market_api.core.order.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.market_api.core.order.dto.OrderResponseDto;
import com.example.market_api.core.order.model.Order;

@Component
public class OrderMapper {

    public OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderStatus(order.getStatus())
                .pickupCode(order.getPickUpcode())
                .orderTotal(order.getOrderTotal())
                .companyName(order.getCompany().getCompanyName())
                .build();

        return orderResponseDto;
    }

    public List<OrderResponseDto> toResponseDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
