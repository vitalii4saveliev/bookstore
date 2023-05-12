package com.example.bookstore.service;


import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.impl.DatabaseUserService;
import com.example.bookstore.util.enums.Role;
import com.example.bookstore.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DatabaseUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new DatabaseUserService(userRepository, userMapper);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        String username = "john";
        User user = new User("john", "password", "john@example.com", Role.ROLE_USER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        org.springframework.security.core.userdetails.UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        GrantedAuthority actualRole = userDetails.getAuthorities().stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("User has no authorities"));
        assertEquals("ROLE_USER", actualRole.getAuthority());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "john";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testFindUserByUsername_UserExists() {
        String username = "john";
        User user = new User("john", "password", "john@example.com", Role.ROLE_USER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByUsername(username);

        assertNotNull(foundUser);
        assertEquals("john", foundUser.getUsername());
        assertEquals("password", foundUser.getPassword());
        assertEquals("john@example.com", foundUser.getEmail());
        assertEquals("ROLE_USER", foundUser.getRole().name());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testFindUserByUsername_UserNotFound() {
        String username = "john";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User foundUser = userService.findUserByUsername(username);

        assertNull(foundUser);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO("john", "password", "john@example.com", Role.ROLE_USER);
        User user = new User("john", "password", "john@example.com", Role.ROLE_USER);

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("john", createdUser.getUsername());
        assertEquals("password", createdUser.getPassword());
        assertEquals("john@example.com", createdUser.getEmail());
        assertEquals("ROLE_USER", createdUser.getRole().name());

        verify(userMapper, times(1)).toEntity(userDTO);
        verify(userRepository, times(1)).save(user);
    }
}
