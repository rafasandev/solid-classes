package com.example.solid_classes.core.role.ports;

import com.example.solid_classes.common.ports.NamedCrudPort;
import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;

public interface RolePort extends NamedCrudPort<Role> {

    Role getByRoleName(RoleName roleName);
}
