package com.example.solid_classes.core.user.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.user.mapper.UserMapper;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.ports.UserPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public User getByEmail(String email) {
        return userPort.getByEmail(email);
    }

    public User getLoggedInUser() {
        return userPort.getLoggedInUser();
    }

    public User signUp(String email, String password) {
        String passwordEncoded = passwordEncoder.encode(password);
        User user = userMapper.toEntity(email, passwordEncoded, Set.of());
        return userPort.save(user);
    }

    public User signUp(String email, String password, Set<Role> roles) {
        String passwordEncoded = passwordEncoder.encode(password);
        User user = userMapper.toEntity(email, passwordEncoded, roles);
        return userPort.save(user);
    }

    public User adminSignUp(String email, String password, Set<Role> roles) {
        String passwordEncoded = passwordEncoder.encode(password);
        User user = userMapper.toEntity(email, passwordEncoded, roles);
        return userPort.save(user);
    }
}
