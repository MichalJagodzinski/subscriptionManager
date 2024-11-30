package com.example.subscriptionManager.services;

import com.example.subscriptionManager.dto.LoginRequestDTO;
import com.example.subscriptionManager.entity.User;
import com.example.subscriptionManager.repository.UserRepository;
import com.example.subscriptionManager.security.JwtTokenProvider;
import com.example.subscriptionManager.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password123");
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user)).thenReturn("mockedToken");

        String token = authenticationService.authenticate(loginRequest);

        assertEquals("mockedToken", token);
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), user.getPasswordHash());
        verify(jwtTokenProvider, times(1)).generateToken(user);
    }

    @Test
    void authenticate_ShouldThrowException_WhenEmailIsInvalid() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("invalid@example.com", "password123");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.authenticate(loginRequest));
        assertEquals("invalid email address or password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsInvalid() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "wrongPassword");
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.authenticate(loginRequest));
        assertEquals("invalid email address or password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), user.getPasswordHash());
        verifyNoInteractions(jwtTokenProvider);
    }
}
