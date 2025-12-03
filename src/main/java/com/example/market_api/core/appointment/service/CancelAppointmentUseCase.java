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
public class CancelAppointmentUseCase {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public AppointmentResponseDto cancelAppointment(UUID appointmentId) {
        Appointment appointment = appointmentService.getById(appointmentId);
        
        // Valida se o usuário logado é o cliente do agendamento
        User loggedUser = userService.getLoggedInUser();
        appointmentService.validateOwnership(appointment, loggedUser.getId(), false);

        // Valida se o agendamento pode ser cancelado
        appointmentService.validateCanBeCancelled(appointment);

        // Cancela o agendamento
        appointment.cancel();
        Appointment saved = appointmentService.save(appointment);
        
        return appointmentMapper.toResponseDto(saved);
    }
}
