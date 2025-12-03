package com.example.market_api.core.appointment.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.appointment.model.enums.AppointmentType;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "appointments")
@SuperBuilder
@Getter
@NoArgsConstructor
public class Appointment extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private AppointmentStatus status;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Referência ao pedido (quando type = PRODUCT_PICKUP)
    @Column(name = "order_id")
    private UUID orderId;

    // Referência ao serviço (quando type = SERVICE)
    @Column(name = "service_id")
    private UUID serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private IndividualProfile customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyProfile company;

    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
    }

    public void markAsNoShow() {
        this.status = AppointmentStatus.NO_SHOW;
    }

    public boolean isPending() {
        return this.status == AppointmentStatus.PENDING;
    }

    public boolean isConfirmed() {
        return this.status == AppointmentStatus.CONFIRMED;
    }

    public boolean canBeCancelled() {
        return this.status == AppointmentStatus.PENDING || this.status == AppointmentStatus.CONFIRMED;
    }

    public boolean isForService() {
        return this.type == AppointmentType.SERVICE;
    }

    public boolean isForProductPickup() {
        return this.type == AppointmentType.PRODUCT_PICKUP;
    }

    public void reschedule(LocalDateTime newDate, String newNotes) {
        this.scheduledDate = newDate;
        this.notes = newNotes;
    }
}
