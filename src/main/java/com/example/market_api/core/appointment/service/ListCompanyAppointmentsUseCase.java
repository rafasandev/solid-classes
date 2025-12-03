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
public class ListCompanyAppointmentsUseCase {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<AppointmentResponseDto> listCompanyAppointments(AppointmentStatus status, Pageable pageable) {
        var companyId = userService.getLoggedInUser().getId();
        Page<Appointment> appointmentsPage = (status != null)
                ? appointmentService.findByCompanyIdAndStatus(companyId, status, pageable)
                : appointmentService.findByCompanyId(companyId, pageable);

        return appointmentsPage.map(appointmentMapper::toResponseDto);
    }
}
