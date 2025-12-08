package com.example.market_api.core.contact_info.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.contact_info.dto.ContactInfoForm;
import com.example.market_api.core.contact_info.mapper.ContactInfoMapper;
import com.example.market_api.core.contact_info.model.ContactInfo;
import com.example.market_api.core.contact_info.ports.ContactInfoPort;
import com.example.market_api.core.contact_type.model.ContactType;
import com.example.market_api.core.contact_type.service.ContactTypeService;
import com.example.market_api.core.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactInfoService {

	private final ContactInfoPort contactInfoPort;
	private final ContactTypeService contactTypeService;
	private final ContactInfoMapper contactInfoMapper;

	@Transactional
	public List<ContactInfo> replaceContacts(User user, List<ContactInfoForm> contactForms) {
		contactInfoPort.deleteByProfileId(user.getId());

		if (contactForms == null || contactForms.isEmpty()) {
			return Collections.emptyList();
		}

		List<ContactInfo> newContacts = contactForms.stream()
				.map(form -> buildContact(user, form))
				.toList();

		return contactInfoPort.saveAll(newContacts);
	}

	private ContactInfo buildContact(User user, ContactInfoForm contactForm) {
		ContactType contactType = contactTypeService.getById(contactForm.getContactTypeId());
		String sanitizedValue = sanitizeValue(contactForm.getValue());
		validateContactValue(contactType, sanitizedValue);
		return contactInfoMapper.toEntity(sanitizedValue, contactType, user);
	}

	private String sanitizeValue(String rawValue) {
		if (rawValue == null) {
			throw new BusinessRuleException("O valor do contato é obrigatório");
		}

		String sanitized = rawValue.trim();
		if (sanitized.isEmpty()) {
			throw new BusinessRuleException("O valor do contato é obrigatório");
		}
		return sanitized;
	}

	private void validateContactValue(ContactType contactType, String value) {
		String regex = contactType.getValidationRegex();
		if (regex != null && !value.matches(regex)) {
			throw new BusinessRuleException(
					String.format("O valor informado não segue o padrão exigido para %s", contactType.getChannel()));
		}
	}
}
