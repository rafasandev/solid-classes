package com.example.solid_classes.core.role.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.role.model.enums.RoleName;
import com.example.solid_classes.core.role.repository.RoleRepository;
import com.example.solid_classes.core.user.service.UserSeeder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner{
    
    private final RoleRepository roleRepository;
    private final UserSeeder userSeeder;

    @Override
    public void run(String... args) {
        for (RoleName roleName : RoleName.values()){
            if (roleRepository.findByName(roleName).isEmpty()){
                Role newRole = Role.create(roleName);
                roleRepository.save(newRole);
            }
        }
        userSeeder.run();
    }
}
