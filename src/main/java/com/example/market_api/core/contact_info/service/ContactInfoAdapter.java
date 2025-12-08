package com.example.market_api.core.contact_info.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.contact_info.model.ContactInfo;
import com.example.market_api.core.contact_info.ports.ContactInfoPort;
import com.example.market_api.core.contact_info.repository.ContactInfoRepository;

@Component
public class ContactInfoAdapter extends NamedCrudAdapter<ContactInfo, ContactInfoRepository> implements ContactInfoPort {
    
    public ContactInfoAdapter(ContactInfoRepository repository) {
        super(repository, "Meio de Contato");
    }

    @Override
    public void deleteByProfileId(UUID profileId) {
        repository.deleteByProfileId(profileId);
    }
}
