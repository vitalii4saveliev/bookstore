package com.example.bookstore.util.mapper;

import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.entity.User;
import com.example.bookstore.util.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userMapper = new UserMapper(passwordEncoder);
    }

    @Test
    public void testToEntity() {
        UserDTO userDTO = new UserDTO("username", "password", "email@mail.com", Role.ROLE_USER);

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(encodedPassword);

        User user = userMapper.toEntity(userDTO);

        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getRole(), user.getRole());

        Mockito.verify(passwordEncoder).encode(userDTO.getPassword());
    }

    @Test
    public void testToDTO() {
        User user = new User("username", "password", "email@mail.com", Role.ROLE_USER);

        UserDTO userDTO = userMapper.toDTO(user);

        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getRole(), userDTO.getRole());
    }
}
