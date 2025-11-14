package com.example.solid_classes.core.user.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.solid_classes.core.role.model.Role;
import com.example.solid_classes.core.user.interfaces.UserPort;
import com.example.solid_classes.core.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public User getByEmail(String email) {
        return userPort.getByEmail(email);
    }

    public User getLoggedInUser() {
        return userPort.getLoggedInUser();
    }

    public User signUp(String email, String password) {
        String passwordEncoded = passwordEncoder.encode(password);
        User user = User.create(email, passwordEncoded, true, new HashSet<>());

        return userPort.save(user);
    }

    public User adminSignUp(String email, String password, Set<Role> roles) {
        String passwordEncoded = passwordEncoder.encode(password);

        User newAdmin = User.create(email, passwordEncoded, true, roles);
        return userPort.save(newAdmin);
    }
}
