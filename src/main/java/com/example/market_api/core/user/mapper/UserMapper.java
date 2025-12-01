package com.example.market_api.core.user.mapper;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.example.market_api.core.role.model.Role;
import com.example.market_api.core.user.dto.UserResponseDto;
import com.example.market_api.core.user.model.User;

@Component
public class UserMapper {

    public User toEntity(String email, String password, Set<Role> roles) {
        User user = User.builder()
                .email(email)
                .password(password)
                .active(true)
                .roles(roles)
                .build();
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
        return responseDto;
    }
}
