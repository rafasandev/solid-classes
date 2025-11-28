package com.example.solid_classes.core.role.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, UUID>{
    Optional<Role> findByName(RoleName name);
}
