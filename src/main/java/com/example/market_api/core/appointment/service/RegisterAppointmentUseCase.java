package com.example.market_api.core.appointment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.core.appointment.dto.AppointmentForm;
import com.example.market_api.core.appointment.dto.AppointmentResponseDto;
import com.example.market_api.core.appointment.mapper.AppointmentMapper;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.profile.service.company.CompanyProfileService;
import com.example.market_api.core.profile.service.individual.IndividualProfileService;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterAppointmentUseCase {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final IndividualProfileService individualProfileService;
    private final CompanyProfileService companyProfileService;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public AppointmentResponseDto registerAppointment(AppointmentForm form) {
        // Obtém o usuário logado (cliente)
        User loggedUser = userService.getLoggedInUser();
        IndividualProfile customer = individualProfileService.getById(loggedUser.getId());

        // Valida se o perfil está ativo
        individualProfileService.validateIsActive(customer);

        // Obtém a empresa
        CompanyProfile company = companyProfileService.getById(form.getCompanyId());
        companyProfileService.validateIsActive(company);

        // Validações de negócio
        appointmentService.validateScheduledDateInFuture(form.getScheduledDate());
        appointmentService.validateAppointmentType(form.getType(), form.getOrderId(), form.getServiceId());

        // Cria e salva o agendamento
        Appointment appointment = appointmentMapper.toEntity(form, customer, company);
        Appointment savedAppointment = appointmentService.save(appointment);

        return appointmentMapper.toResponseDto(savedAppointment);
    }
}
