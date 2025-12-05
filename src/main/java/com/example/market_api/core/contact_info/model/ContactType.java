package com.example.market_api.core.contact_info.model;

import java.util.List;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.contact_info.model.enums.ContactChannel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "contact_methods")
@Getter
@SuperBuilder
@NoArgsConstructor
public class ContactType extends AuditableEntity {
    @Column(nullable = false, unique = true)
    private ContactChannel channel;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String baseUrl;

    @Column(nullable = false)
    private String validationRegex;

    @Column(nullable = false)
    private String iconUrl;

    @OneToMany(mappedBy = "contactType", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContactInfo> contactInfos;
}
