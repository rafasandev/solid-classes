package com.example.market_api.core.appointment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.appointment.model.enums.AppointmentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {

    private UUID id;
    private AppointmentType type;
    private AppointmentStatus status;
    private LocalDateTime scheduledDate;
    private String notes;
    private UUID orderId;
    private UUID serviceId;
    private UUID customerId;
    private String customerName;
    private UUID companyId;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
