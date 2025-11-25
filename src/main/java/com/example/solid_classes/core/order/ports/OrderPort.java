package com.example.solid_classes.core.order.ports;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.order.model.Order;

public interface OrderPort extends NamedCrudPort<Order>{
    
    boolean existsByPickUpcode(String pickUpcode);
}
