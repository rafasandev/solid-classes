package com.example.market_api.core.role.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.market_api.core.role.model.Role;
import com.example.market_api.core.role.model.enums.RoleName;
import com.example.market_api.core.role.repository.jpa.RoleRepository;
import com.example.market_api.core.user.service.UserSeeder;

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
