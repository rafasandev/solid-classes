package com.example.market_api.core.appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.appointment.model.enums.AppointmentType;
import com.example.market_api.core.appointment.ports.AppointmentPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentPort appointmentPort;

    public Appointment getById(UUID id) {
        return appointmentPort.getById(id);
    }

    public Appointment save(Appointment appointment) {
        return appointmentPort.save(appointment);
    }

    public void delete(Appointment appointment) {
        appointmentPort.delete(appointment);
    }

    public Page<Appointment> findByCustomerId(UUID customerId, Pageable pageable) {
        return appointmentPort.findByCustomerId(customerId, pageable);
    }

    public Page<Appointment> findByCompanyId(UUID companyId, Pageable pageable) {
        return appointmentPort.findByCompanyId(companyId, pageable);
    }

    public List<Appointment> findByOrderId(UUID orderId) {
        return appointmentPort.findByOrderId(orderId);
    }

    public List<Appointment> findByServiceId(UUID serviceId) {
        return appointmentPort.findByServiceId(serviceId);
    }

    public Page<Appointment> findByCompanyIdAndStatus(UUID companyId, AppointmentStatus status, Pageable pageable) {
        return appointmentPort.findByCompanyIdAndStatus(companyId, status, pageable);
    }

    public Page<Appointment> findByCustomerIdAndStatus(UUID customerId, AppointmentStatus status, Pageable pageable) {
        return appointmentPort.findByCustomerIdAndStatus(customerId, status, pageable);
    }

    public List<Appointment> findByCompanyIdAndScheduledDateBetween(UUID companyId, LocalDateTime start, 
            LocalDateTime end) {
        return appointmentPort.findByCompanyIdAndScheduledDateBetween(companyId, start, end);
    }

    public void validateScheduledDateInFuture(LocalDateTime scheduledDate) {
        if (scheduledDate.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("A data de agendamento deve ser no futuro");
        }
    }

    public void validateAppointmentType(AppointmentType type, UUID orderId, UUID serviceId) {
        if (type == AppointmentType.PRODUCT_PICKUP && orderId == null) {
            throw new BusinessRuleException("Para agendamento de retirada de produto, o ID do pedido é obrigatório");
        }
        if (type == AppointmentType.SERVICE && serviceId == null) {
            throw new BusinessRuleException("Para agendamento de serviço, o ID do serviço é obrigatório");
        }
    }

    public void validateCanBeCancelled(Appointment appointment) {
        if (!appointment.canBeCancelled()) {
            throw new BusinessRuleException("Este agendamento não pode ser cancelado no status atual");
        }
    }

    public void validateCanBeModified(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.COMPLETED || 
            appointment.getStatus() == AppointmentStatus.CANCELLED ||
            appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new BusinessRuleException("Agendamentos finalizados não podem ser modificados");
        }
    }

    public void validateOwnership(Appointment appointment, UUID userId, boolean isCompany) {
        if (isCompany) {
            if (!appointment.getCompany().getId().equals(userId)) {
                throw new BusinessRuleException("Você não tem permissão para acessar este agendamento");
            }
        } else {
            if (!appointment.getCustomer().getId().equals(userId)) {
                throw new BusinessRuleException("Você não tem permissão para acessar este agendamento");
            }
        }
    }
}
