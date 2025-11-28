package com.example.solid_classes.auth.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.solid_classes.auth.model.login.UserLoginForm;
import com.example.solid_classes.auth.model.login.UserLoginResponse;
import com.example.solid_classes.core.role.model.enums.RoleName;
import com.example.solid_classes.core.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserLoginResponse authenticateLogin(UserLoginForm loginForm) {
        User authenticatedUser = authenticate(loginForm);
        Map<String, Object> extraClaims = new java.util.HashMap<>();

        String accountType = "INDIVIDUAL";
        if (authenticatedUser.getRoles() != null) {
            if (authenticatedUser.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ADMIN)) {
                accountType = "ADMIN";
            } else if (authenticatedUser.getRoles().stream().anyMatch(r -> r.getName() == RoleName.COMPANY)) {
                accountType = "COMPANY";
            }
        }

        extraClaims.put("accountType", accountType);

        List<String> rolesClaim = new java.util.ArrayList<>();
        if (authenticatedUser.getRoles() != null) {
            for (var r : authenticatedUser.getRoles()) {
                switch (r.getName()) {
                    case ADMIN:
                        rolesClaim.add("ROLE_ADMIN");
                        break;
                    case COMPANY:
                        rolesClaim.add("ROLE_COMPANY");
                        break;
                    case INDIVIDUAL:
                        rolesClaim.add("ROLE_INDIVIDUAL");
                        break;
                    default:
                        rolesClaim.add("ROLE_" + r.getName().name());
                }
            }
        }

        extraClaims.put("roles", rolesClaim);

        String token = jwtService.generateToken(extraClaims, authenticatedUser);
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
