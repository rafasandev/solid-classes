package com.example.market_api.core.profile.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.market_api.core.contact_info.dto.ContactInfoResponseDto;
import com.example.market_api.core.contact_info.model.ContactInfo;
import com.example.market_api.core.contact_type.model.ContactType;
import com.example.market_api.core.payment_method.dto.PaymentMethodResponseDto;
import com.example.market_api.core.payment_method.model.PaymentMethod;
import com.example.market_api.core.profile.dto.company.CompanyProfileForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.dto.individual.IndividualProfileForm;
import com.example.market_api.core.profile.dto.individual.IndividualProfileResponseDto;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.value.CompanyDailyAvailability;
import com.example.market_api.core.profile.model.company.value.TimeRange;
import com.example.market_api.core.profile.model.individual.IndividualProfile;
import com.example.market_api.core.user.model.User;

@Component
public class ProfileMapper {

    public CompanyProfile toEntity(CompanyProfileForm profileForm, User user) {
        return CompanyProfile.builder()
                .user(user)
                .companyName(profileForm.getCompanyName())
                .cnpj(profileForm.getCnpj())
                .businessSector(profileForm.getBusinessSector())
                .active(true)
                .build();
    }

    public IndividualProfile toEntity(IndividualProfileForm profileForm, User user) {
        return IndividualProfile.builder()
                .user(user)
                .name(profileForm.getName())
                .cpf(profileForm.getCpf())
                .active(true)
                .build();
    }

    public CompanyProfileResponseDto toResponseDto(CompanyProfile savedProfile) {
        return CompanyProfileResponseDto.builder()
                .id(savedProfile.getId())
                .companyName(savedProfile.getCompanyName())
                .cnpj(savedProfile.getCnpj())
                .businessSector(savedProfile.getBusinessSector())
                .contactMethods(mapContactInfos(savedProfile.getUser()))
                .acceptedPaymentMethods(mapPaymentMethods(savedProfile.getPaymentMethods()))
                .weekDaysAvailable(savedProfile.getWeekDaysAvailable())
                .dailyAvailableTimeRanges(mapDailyAvailability(savedProfile.getDailyAvailableTimeRanges()))
                .build();
    }

    public IndividualProfileResponseDto toResponseDto(IndividualProfile savedProfile) {
        return IndividualProfileResponseDto.builder()
                .id(savedProfile.getId())
                .name(savedProfile.getName())
                .cpf(savedProfile.getCpf())
                .contactMethods(mapContactInfos(savedProfile.getUser()))
                .build();
    }

    private List<ContactInfoResponseDto> mapContactInfos(User user) {
        if (user == null || user.getContacts() == null || user.getContacts().isEmpty()) {
            return Collections.emptyList();
        }

        return user.getContacts().stream()
            .map(this::toContactResponse)
            .filter(Objects::nonNull)
            .toList();
    }

    private ContactInfoResponseDto toContactResponse(ContactInfo contact) {
        if (contact == null) {
            return null;
        }

        ContactType contactType = contact.getContactType();
        return ContactInfoResponseDto.builder()
                .channel(contactType != null ? contactType.getChannel() : null)
                .baseUrl(contactType != null ? contactType.getBaseUrl() : null)
                .iconUrl(contactType != null ? contactType.getIconUrl() : null)
                .value(contact.getValue())
                .build();
    }

    private Set<PaymentMethodResponseDto> mapPaymentMethods(Set<PaymentMethod> paymentMethods) {
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            return Collections.emptySet();
        }

        return paymentMethods.stream()
                .map(method -> PaymentMethodResponseDto.builder()
                        .id(method.getId())
                        .name(method.getName())
                        .iconUrl(method.getIconUrl())
                        .build())
                .collect(Collectors.toSet());
    }

    private Map<Integer, List<TimeRange>> mapDailyAvailability(List<CompanyDailyAvailability> availabilitySlots) {
        if (availabilitySlots == null || availabilitySlots.isEmpty()) {
            return Collections.emptyMap();
        }

        return availabilitySlots.stream()
                .collect(Collectors.groupingBy(CompanyDailyAvailability::getWeekDay,
                        Collectors.mapping(slot -> new TimeRange(slot.getStartTime(), slot.getEndTime()), Collectors.toList())));
    }
}
