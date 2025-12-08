package com.example.market_api.core.profile.dto.company;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompanyDailyAvailabilityForm {

    @NotNull(message = "O dia da semana é obrigatório")
    @Min(value = 0, message = "O dia da semana deve estar entre 0 e 6")
    @Max(value = 6, message = "O dia da semana deve estar entre 0 e 6")
    private Integer weekDay;

    @NotEmpty(message = "Informe pelo menos um intervalo para o dia informado")
    @Valid
    private List<TimeRangeForm> timeRanges;
}
