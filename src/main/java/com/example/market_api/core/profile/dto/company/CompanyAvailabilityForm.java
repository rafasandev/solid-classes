package com.example.market_api.core.profile.dto.company;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CompanyAvailabilityForm {

    @NotEmpty(message = "Informe pelo menos um dia da semana disponível")
    private List<@Min(value = 0, message = "O dia da semana deve estar entre 0 e 6")
                @Max(value = 6, message = "O dia da semana deve estar entre 0 e 6") Integer> weekDaysAvailable;

    @NotEmpty(message = "Informe os horários disponíveis para cada dia")
    @Valid
    private List<CompanyDailyAvailabilityForm> dailyAvailability;
}
