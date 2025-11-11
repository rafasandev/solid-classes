package com.example.solid_classes.core.user.service;

import org.springframework.stereotype.Service;

import com.example.solid_classes.common.abs.CrudService;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService extends CrudService<User, UserRepository> {

    @Override
    protected String getEntityName() {
        return "Usuário";
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(this::throwEntityNotFound);
    }



}
