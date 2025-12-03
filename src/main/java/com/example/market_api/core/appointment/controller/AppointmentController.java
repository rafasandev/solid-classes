package com.example.market_api.core.appointment.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.market_api.core.appointment.dto.AppointmentForm;
import com.example.market_api.core.appointment.dto.AppointmentResponseDto;
import com.example.market_api.core.appointment.dto.UpdateAppointmentForm;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.appointment.service.CancelAppointmentUseCase;
import com.example.market_api.core.appointment.service.CompleteAppointmentUseCase;
import com.example.market_api.core.appointment.service.ConfirmAppointmentUseCase;
import com.example.market_api.core.appointment.service.GetAppointmentUseCase;
import com.example.market_api.core.appointment.service.ListCompanyAppointmentsUseCase;
import com.example.market_api.core.appointment.service.ListCustomerAppointmentsUseCase;
import com.example.market_api.core.appointment.service.RegisterAppointmentUseCase;
import com.example.market_api.core.appointment.service.UpdateAppointmentUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Gerenciamento de agendamentos")
public class AppointmentController {

    private final RegisterAppointmentUseCase registerAppointmentUseCase;
    private final GetAppointmentUseCase getAppointmentUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final CancelAppointmentUseCase cancelAppointmentUseCase;
    private final ConfirmAppointmentUseCase confirmAppointmentUseCase;
    private final CompleteAppointmentUseCase completeAppointmentUseCase;
    private final ListCustomerAppointmentsUseCase listCustomerAppointmentsUseCase;
    private final ListCompanyAppointmentsUseCase listCompanyAppointmentsUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_INDIVIDUAL')")
    @Operation(summary = "Criar novo agendamento", description = "Cliente cria um agendamento para serviço ou retirada de produto")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody AppointmentForm form) {
        AppointmentResponseDto response = registerAppointmentUseCase.registerAppointment(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_INDIVIDUAL', 'ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os detalhes de um agendamento específico")
    public ResponseEntity<AppointmentResponseDto> getAppointment(@PathVariable UUID id) {
        AppointmentResponseDto response = getAppointmentUseCase.getAppointment(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_INDIVIDUAL')")
    @Operation(summary = "Listar agendamentos do cliente", description = "Lista todos os agendamentos do cliente logado")
    public ResponseEntity<Page<AppointmentResponseDto>> getCustomerAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AppointmentResponseDto> appointments = listCustomerAppointmentsUseCase.listCustomerAppointments(status, pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @Operation(summary = "Listar agendamentos da empresa", description = "Lista todos os agendamentos da empresa logada")
    public ResponseEntity<Page<AppointmentResponseDto>> getCompanyAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AppointmentResponseDto> appointments = listCompanyAppointmentsUseCase.listCompanyAppointments(status, pageable);
        return ResponseEntity.ok(appointments);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_INDIVIDUAL')")
    @Operation(summary = "Atualizar agendamento", description = "Cliente atualiza data ou observações do agendamento")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentForm form) {
        AppointmentResponseDto response = updateAppointmentUseCase.updateAppointment(id, form);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ROLE_INDIVIDUAL')")
    @Operation(summary = "Cancelar agendamento", description = "Cliente cancela um agendamento")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(@PathVariable UUID id) {
        AppointmentResponseDto response = cancelAppointmentUseCase.cancelAppointment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @Operation(summary = "Confirmar agendamento", description = "Empresa confirma um agendamento pendente")
    public ResponseEntity<AppointmentResponseDto> confirmAppointment(@PathVariable UUID id) {
        AppointmentResponseDto response = confirmAppointmentUseCase.confirmAppointment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @Operation(summary = "Completar agendamento", description = "Empresa marca agendamento como concluído")
    public ResponseEntity<AppointmentResponseDto> completeAppointment(@PathVariable UUID id) {
        AppointmentResponseDto response = completeAppointmentUseCase.completeAppointment(id);
        return ResponseEntity.ok(response);
    }
}
