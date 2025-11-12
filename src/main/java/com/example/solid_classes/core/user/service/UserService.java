package com.example.solid_classes.core.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.solid_classes.common.abs.CrudService;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService extends CrudService<User, UserRepository> {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected String getEntityName() {
        return "Usuário";
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(this::throwEntityNotFound);
    }

    public User signUp(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .active(true)
                .build();

        return save(user);
    }

}
