package com.example.market_api.core.appointment.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
    Page<Appointment> findByCustomerId(UUID customerId, Pageable pageable);
    
    Page<Appointment> findByCompanyId(UUID companyId, Pageable pageable);
    
    List<Appointment> findByOrderId(UUID orderId);
    
    List<Appointment> findByServiceId(UUID serviceId);
    
    Page<Appointment> findByCompanyIdAndStatus(UUID companyId, AppointmentStatus status, Pageable pageable);
    
    Page<Appointment> findByCustomerIdAndStatus(UUID customerId, AppointmentStatus status, Pageable pageable);
    
    List<Appointment> findByCompanyIdAndScheduledDateBetween(UUID companyId, LocalDateTime start, LocalDateTime end);
}
