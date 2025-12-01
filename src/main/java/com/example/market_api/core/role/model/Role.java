package com.example.market_api.core.role.model;

import java.util.HashSet;
import java.util.Set;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.role.model.enums.RoleName;
import com.example.market_api.core.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends AuditableEntity{

    private Role(RoleName name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public static Role create(RoleName roleName) {
        return new Role(roleName);
    }
}
