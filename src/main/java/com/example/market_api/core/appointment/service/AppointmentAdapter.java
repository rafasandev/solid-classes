package com.example.market_api.core.appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.appointment.ports.AppointmentPort;
import com.example.market_api.core.appointment.repository.jpa.AppointmentRepository;

@Component
public class AppointmentAdapter extends NamedCrudAdapter<Appointment, AppointmentRepository> 
        implements AppointmentPort {

    public AppointmentAdapter(AppointmentRepository repository) {
        super(repository, "Agendamento");
    }

    @Override
    public Page<Appointment> findByCustomerId(UUID customerId, Pageable pageable) {
        return repository.findByCustomerId(customerId, pageable);
    }

    @Override
    public Page<Appointment> findByCompanyId(UUID companyId, Pageable pageable) {
        return repository.findByCompanyId(companyId, pageable);
    }

    @Override
    public List<Appointment> findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId);
    }

    @Override
    public List<Appointment> findByServiceId(UUID serviceId) {
        return repository.findByServiceId(serviceId);
    }

    @Override
    public Page<Appointment> findByCompanyIdAndStatus(UUID companyId, AppointmentStatus status, Pageable pageable) {
        return repository.findByCompanyIdAndStatus(companyId, status, pageable);
    }

    @Override
    public Page<Appointment> findByCustomerIdAndStatus(UUID customerId, AppointmentStatus status, Pageable pageable) {
        return repository.findByCustomerIdAndStatus(customerId, status, pageable);
    }

    @Override
    public List<Appointment> findByCompanyIdAndScheduledDateBetween(UUID companyId, LocalDateTime start, 
            LocalDateTime end) {
        return repository.findByCompanyIdAndScheduledDateBetween(companyId, start, end);
    }
}
