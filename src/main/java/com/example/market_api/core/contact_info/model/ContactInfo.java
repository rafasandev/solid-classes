package com.example.market_api.core.contact_info.model;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "contact_types")
@SuperBuilder
@Getter
@NoArgsConstructor
public class ContactInfo extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_method_id", nullable = false)
    private ContactType contactType;
    
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User profile;
}
