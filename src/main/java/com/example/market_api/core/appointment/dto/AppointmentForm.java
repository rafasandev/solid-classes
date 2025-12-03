package com.example.market_api.core.appointment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.market_api.core.appointment.model.enums.AppointmentType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentForm {

    @NotNull(message = "O tipo de agendamento é obrigatório")
    private AppointmentType type;

    @NotNull(message = "A data agendada é obrigatória")
    @Future(message = "A data agendada deve ser no futuro")
    private LocalDateTime scheduledDate;

    private String notes;

    // Para agendamento de retirada de produto
    private UUID orderId;

    // Para agendamento de serviço
    private UUID serviceId;

    @NotNull(message = "O ID da empresa é obrigatório")
    private UUID companyId;
}
