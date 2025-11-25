package com.example.solid_classes.core.order.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.order.dto.OrderResponseDto;
import com.example.solid_classes.core.order.model.Order;

@Component
public class OrderMapper {

    public OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderStatus(order.getStatus())
                .pickupCode(order.getPickUpcode())
                .build();

        return orderResponseDto;
    }

    public List<OrderResponseDto> toResponseDtoList(List<Order> orders) {
        return orders.stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }
}
