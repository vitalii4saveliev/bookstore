package com.example.bookstore.service;

import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByUsername(String username);

    User createUser(UserDTO userDTO);

}
