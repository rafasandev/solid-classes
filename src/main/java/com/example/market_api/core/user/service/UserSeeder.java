package com.example.market_api.core.user.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.market_api.core.role.model.Role;
import com.example.market_api.core.role.model.enums.RoleName;
import com.example.market_api.core.role.service.RoleService;
import com.example.market_api.core.user.repository.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public void run() {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Role adminRole = roleService.getByRoleName(RoleName.ADMIN);
            userService.adminSignUp(adminEmail, adminPassword, Set.of(adminRole));
        }
    }
}
