package com.example.solid_classes.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solid_classes.auth.model.login.UserLoginForm;
import com.example.solid_classes.auth.model.login.UserLoginResponse;
import com.example.solid_classes.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(@Valid @RequestBody UserLoginForm loginForm) {
        UserLoginResponse loginResponse = authService.authenticateLogin(loginForm);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

}
