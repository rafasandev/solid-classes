package com.example.solid_classes.core.order.service;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.order.model.Order;
import com.example.solid_classes.core.order.ports.OrderPort;
import com.example.solid_classes.core.order.repository.jpa.OrderRepository;

@Component
public class OrderAdapter extends NamedCrudAdapter<Order, OrderRepository> implements OrderPort {

    public OrderAdapter(OrderRepository orderRepository) {
        super(orderRepository, "Pedido");
    }

    @Override
    public boolean existsByPickUpcode(String pickUpcode) {
        return repository.existsByPickUpcode(pickUpcode);
    }
}
