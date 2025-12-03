package com.example.market_api.core.appointment.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.appointment.dto.AppointmentResponseDto;
import com.example.market_api.core.appointment.mapper.AppointmentMapper;
import com.example.market_api.core.appointment.model.Appointment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAppointmentUseCase {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @Transactional(readOnly = true)
    public AppointmentResponseDto getAppointment(UUID appointmentId) {
        Appointment appointment = appointmentService.getById(appointmentId);
        return appointmentMapper.toResponseDto(appointment);
    }
}
