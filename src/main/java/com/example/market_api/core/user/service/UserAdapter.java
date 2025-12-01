package com.example.market_api.core.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.market_api.common.base.NamedCrudAdapter;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.ports.UserPort;
import com.example.market_api.core.user.repository.jpa.UserRepository;

@Component
public class UserAdapter extends NamedCrudAdapter<User, UserRepository> implements UserPort {

    public UserAdapter(UserRepository userRepository) {
        super(userRepository, "Usuário");
    }

    @Override
    public User getByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(this::throwEntityNotFound);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return null;

        Object principal = authentication.getPrincipal();
        String username = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        User loggedInUser = getByEmail(username);
        return loggedInUser;
    }
}
