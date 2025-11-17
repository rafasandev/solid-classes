package com.example.solid_classes.core.role.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.base.NamedCrudAdapter;
import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;
import com.example.solid_classes.core.role.ports.RolePort;
import com.example.solid_classes.core.role.repository.RoleRepository;

@Service
public class RoleAdapter extends NamedCrudAdapter<Role, RoleRepository> implements RolePort {

    public RoleAdapter(RoleRepository roleRepository) {
        super(roleRepository, "Função");
    }

    @Override
    public Role getByRoleName(RoleName roleName) {
        return repository.findByName(roleName).orElseThrow(this::throwEntityNotFound);
    }

}
