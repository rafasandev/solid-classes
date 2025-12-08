package com.example.market_api.core.contact_info.mapper;

import org.springframework.stereotype.Component;

import com.example.market_api.core.contact_info.dto.ContactInfoResponseDto;
import com.example.market_api.core.contact_info.model.ContactInfo;
import com.example.market_api.core.contact_type.model.ContactType;
import com.example.market_api.core.user.model.User;

@Component
public class ContactInfoMapper {
    
    public ContactInfo toEntity(String value, ContactType contactType, User user) {
        return ContactInfo.builder()
                .value(value)
                .contactType(contactType)
                .profile(user)
                .build();
    }

    public ContactInfoResponseDto toResponseDto(ContactInfo contactInfo, ContactType contactType) {
        return ContactInfoResponseDto.builder()
                .channel(contactType.getChannel())
                .baseUrl(contactType.getBaseUrl())
                .iconUrl(contactType.getIconUrl())
                .value(contactInfo.getValue())
                .build();
    }

}
