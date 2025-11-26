package com.example.solid_classes.core.user.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;
import com.example.solid_classes.core.role.service.RoleService;
import com.example.solid_classes.core.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public void run() {
        String adminEmail = "admin@uniso.com";
        String adminPassword = "Senh@123";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Role adminRole = roleService.getByRoleName(RoleName.ADMIN);
            userService.adminSignUp(adminEmail, adminPassword, Set.of(adminRole));
        }
    }
}
