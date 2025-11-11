package com.example.solid_classes.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.solid_classes.auth.model.login.UserLoginForm;
import com.example.solid_classes.auth.model.login.UserLoginResponse;
import com.example.solid_classes.auth.model.register.UserRegisterForm;
import com.example.solid_classes.core.user.model.User;
import com.example.solid_classes.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public User signUp(UserRegisterForm registerForm) {
        User user = new User();
        user.setName(registerForm.getName());
        user.setEmail(registerForm.getEmail());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setActive(true);

        return userService.save(user);
    }

    public UserLoginResponse authenticateLogin(UserLoginForm loginForm) {
        User authenticatedUser = authenticate(loginForm);
        String token = jwtService.generateToken(authenticatedUser);
        long expirationTime = jwtService.getExpirationTime();
        return new UserLoginResponse(token, expirationTime);
    }

    private User authenticate(UserLoginForm loginForm) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginForm.getEmail(),
                loginForm.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User authenticatedUser = (User) authentication.getPrincipal();
        return authenticatedUser;
    }
}
