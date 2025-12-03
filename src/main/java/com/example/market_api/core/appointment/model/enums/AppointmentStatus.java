package com.example.market_api.core.appointment.model.enums;

public enum AppointmentStatus {
    PENDING,      // Aguardando confirmação
    CONFIRMED,    // Confirmado pela empresa
    CANCELLED,    // Cancelado
    COMPLETED,    // Concluído
    NO_SHOW       // Cliente não compareceu
}
