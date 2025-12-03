package com.example.market_api.core.appointment.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.appointment.dto.AppointmentResponseDto;
import com.example.market_api.core.appointment.mapper.AppointmentMapper;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmAppointmentUseCase {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public AppointmentResponseDto confirmAppointment(UUID appointmentId) {
        Appointment appointment = appointmentService.getById(appointmentId);
        
        // Valida se o usuário logado é a empresa do agendamento
        User loggedUser = userService.getLoggedInUser();
        appointmentService.validateOwnership(appointment, loggedUser.getId(), true);

        // Confirma o agendamento
        appointment.confirm();
        Appointment saved = appointmentService.save(appointment);
        
        return appointmentMapper.toResponseDto(saved);
    }
}
