package com.example.market_api.core.order.ports;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.order.model.Order;

public interface OrderPort extends NamedCrudPort<Order>{
    
    boolean existsByPickUpcode(String pickUpcode);
}
