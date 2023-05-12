package com.example.bookstore.util.mapper;

import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserDTO userDTO) {
        return new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getEmail(), userDTO.getRole());
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
    }
}
