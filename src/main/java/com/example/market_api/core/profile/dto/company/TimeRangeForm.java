package com.example.market_api.core.profile.dto.company;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TimeRangeForm {

    @NotNull(message = "O horário inicial é obrigatório")
    private LocalTime startTime;

    @NotNull(message = "O horário final é obrigatório")
    private LocalTime endTime;
}
