package com.example.market_api.core.contact_info.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.market_api.core.contact_info.model.ContactInfo;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, UUID> {

	void deleteByProfileId(UUID profileId);
}
