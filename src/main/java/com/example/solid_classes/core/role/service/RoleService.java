package com.example.solid_classes.core.role.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.core.role.interfaces.RolePort;
import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolePort rolePort;

    public Role getByRoleName(RoleName roleName) {
        return rolePort.getByRoleName(roleName);
    }
}
