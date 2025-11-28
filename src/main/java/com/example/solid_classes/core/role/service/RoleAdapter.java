package com.example.solid_classes.core.role.service;

import org.springframework.stereotype.Component;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;
import com.example.solid_classes.core.role.ports.RolePort;
import com.example.solid_classes.core.role.repository.jpa.RoleRepository;

@Component
public class RoleAdapter extends NamedCrudAdapter<Role, RoleRepository> implements RolePort {

    public RoleAdapter(RoleRepository roleRepository) {
        super(roleRepository, "Função | Cargo");
    }

    @Override
    public Role getByRoleName(RoleName roleName) {
        return repository.findByName(roleName).orElseThrow(this::throwEntityNotFound);
    }

}
