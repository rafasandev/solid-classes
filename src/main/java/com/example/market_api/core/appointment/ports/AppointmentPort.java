package com.example.market_api.core.appointment.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;

public interface AppointmentPort extends NamedCrudPort<Appointment> {
    
    Page<Appointment> findByCustomerId(UUID customerId, Pageable pageable);
    
    Page<Appointment> findByCompanyId(UUID companyId, Pageable pageable);
    
    List<Appointment> findByOrderId(UUID orderId);
    
    List<Appointment> findByServiceId(UUID serviceId);
    
    Page<Appointment> findByCompanyIdAndStatus(UUID companyId, AppointmentStatus status, Pageable pageable);
    
    Page<Appointment> findByCustomerIdAndStatus(UUID customerId, AppointmentStatus status, Pageable pageable);
    
    List<Appointment> findByCompanyIdAndScheduledDateBetween(UUID companyId, LocalDateTime start, LocalDateTime end);
}
