package com.example.market_api.core.role.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.market_api.core.role.model.Role;
import com.example.market_api.core.role.model.enums.RoleName;
import com.example.market_api.core.role.ports.RolePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolePort rolePort;

    public Role getById(UUID id) {
        return rolePort.getById(id);
    }

    public Role getByRoleName(RoleName roleName) {
        return rolePort.getByRoleName(roleName);
    }

    public Role save(Role role) {
        return rolePort.save(role);
    }

    public List<Role> findAll() {
        return rolePort.findAll();
    }
}
