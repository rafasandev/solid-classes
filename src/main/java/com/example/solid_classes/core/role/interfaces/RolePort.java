package com.example.solid_classes.core.role.interfaces;

import com.example.solid_classes.common.interfaces.NamedCrudPort;
import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;

public interface RolePort extends NamedCrudPort<Role> {

    Role getByRoleName(RoleName roleName);
}
