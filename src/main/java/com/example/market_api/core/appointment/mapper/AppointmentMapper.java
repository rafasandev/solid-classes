package com.example.market_api.core.appointment.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.appointment.dto.AppointmentForm;
import com.example.market_api.core.appointment.dto.AppointmentResponseDto;
import com.example.market_api.core.appointment.model.Appointment;
import com.example.market_api.core.appointment.model.enums.AppointmentStatus;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.individual.IndividualProfile;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentForm form, IndividualProfile customer, CompanyProfile company) {
        return Appointment.builder()
                .type(form.getType())
                .status(AppointmentStatus.PENDING)
                .scheduledDate(form.getScheduledDate())
                .notes(form.getNotes())
                .orderId(form.getOrderId())
                .serviceId(form.getServiceId())
                .customer(customer)
                .company(company)
                .build();
    }

    public AppointmentResponseDto toResponseDto(Appointment appointment) {
        String customerName = (appointment.getCustomer() != null) 
                ? appointment.getCustomer().getName() 
                : null;
        
        String companyName = (appointment.getCompany() != null) 
                ? appointment.getCompany().getCompanyName() 
                : null;

        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .type(appointment.getType())
                .status(appointment.getStatus())
                .scheduledDate(appointment.getScheduledDate())
                .notes(appointment.getNotes())
                .orderId(appointment.getOrderId())
                .serviceId(appointment.getServiceId())
                .customerId(appointment.getCustomer() != null ? appointment.getCustomer().getId() : null)
                .customerName(customerName)
                .companyId(appointment.getCompany() != null ? appointment.getCompany().getId() : null)
                .companyName(companyName)
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}
