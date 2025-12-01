package com.example.market_api.core.role.ports;

import com.example.market_api.common.ports.NamedCrudPort;
import com.example.market_api.core.role.model.Role;
import com.example.market_api.core.role.model.enums.RoleName;

public interface RolePort extends NamedCrudPort<Role> {

    Role getByRoleName(RoleName roleName);
}
