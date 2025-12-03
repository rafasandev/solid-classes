package com.example.market_api.core.appointment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.appointment.dto.AppointmentResponseDto;
import com.example.market_api.core.appointment.mapper.AppointmentMapper;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListCustomerAppointmentsUseCase {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<AppointmentResponseDto> listCustomerAppointments(AppointmentStatus status, Pageable pageable) {
        var customerId = userService.getLoggedInUser().getId();
        Page<Appointment> appointmentsPage = (status != null)
                ? appointmentService.findByCustomerIdAndStatus(customerId, status, pageable)
                : appointmentService.findByCustomerId(customerId, pageable);

        return appointmentsPage.map(appointmentMapper::toResponseDto);
    }
}
