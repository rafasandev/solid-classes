package com.example.market_api.core.contact_info.ports;

import java.util.UUID;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.contact_info.model.ContactInfo;

public interface ContactInfoPort extends NamedCrudPort<ContactInfo> {

	void deleteByProfileId(UUID profileId);
}
