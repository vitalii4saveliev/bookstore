package com.example.bookstore.controller;

import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.dto.jwt.JwtRequest;
import com.example.bookstore.dto.jwt.JwtResponse;
import com.example.bookstore.entity.User;
import com.example.bookstore.util.JwtTokenUtil;
import com.example.bookstore.service.UserService;
import com.example.bookstore.util.enums.Role;
import com.example.bookstore.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserMapper userMapper;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, userService, jwtTokenUtil, userMapper);
    }

    @Test
    public void testCreateAuthenticationToken() throws Exception {
        JwtRequest authenticationRequest = new JwtRequest("username", "password");
        UserDetails userDetails = new User("username", "password", "email@mail.com", Role.ROLE_USER);
        String token = "token";
        JwtResponse expectedResponse = new JwtResponse(token);

        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(userService.loadUserByUsername(authenticationRequest.getUsername()))
                .thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn(expectedResponse);

        ResponseEntity<JwtResponse> response = authController.createAuthenticationToken(authenticationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(userService).loadUserByUsername(authenticationRequest.getUsername());
        verify(jwtTokenUtil).generateToken(userDetails);
    }
    @Test
    public void testCreateAuthenticationToken_InvalidCredentials() throws Exception {
        JwtRequest authenticationRequest = new JwtRequest("username", "password");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(Exception.class, () -> authController.createAuthenticationToken(authenticationRequest));
        verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testRegisterUser() {
        UserDTO userDto = new UserDTO("username", "password", "email@email.com", Role.ROLE_USER);
        User user = new User("username", "password", "email@mail.com", Role.ROLE_USER);

        Mockito.when(userService.findUserByUsername(userDto.getUsername())).thenReturn(null);
        Mockito.when(userService.createUser(userDto)).thenReturn(user);
        Mockito.when(userMapper.toDTO(user)).thenReturn(userDto);

        ResponseEntity<UserDTO> response = authController.registerUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService).findUserByUsername(userDto.getUsername());
        verify(userService).createUser(userDto);
        verify(userMapper).toDTO(user);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        UserDTO userDto = new UserDTO("username", "password", "email@email.com", Role.ROLE_USER);
        User user = new User("username", "password", "email@mail.com", Role.ROLE_USER);

        Mockito.when(userService.findUserByUsername(userDto.getUsername())).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> authController.registerUser(userDto));
        verify(userService).findUserByUsername(userDto.getUsername());
    }

}
