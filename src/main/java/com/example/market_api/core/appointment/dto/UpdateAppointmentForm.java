package com.example.market_api.core.appointment.dto;

import java.time.LocalDateTime;

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
public class UpdateAppointmentForm {

    @NotNull(message = "A data agendada é obrigatória")
    @Future(message = "A data agendada deve ser no futuro")
    private LocalDateTime scheduledDate;

    private String notes;
}
