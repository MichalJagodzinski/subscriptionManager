package com.example.subscriptionManager.services;

import com.example.subscriptionManager.config.JwtConfig;
import com.example.subscriptionManager.dto.LoginRequestDTO;
import com.example.subscriptionManager.entity.User;
import com.example.subscriptionManager.repository.UserRepository;
import com.example.subscriptionManager.security.JwtTokenProvider;
import com.example.subscriptionManager.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtConfig jwtConfig;  // Mockowanie JwtConfig

    @Mock
    private JwtTokenProvider jwtTokenProvider;  // Mockowanie JwtTokenProvider

    @InjectMocks
    private AuthenticationService authenticationService;  // Tworzymy instancję AuthenticationService

    @Test
    public void shouldAuthenticateUserAndGenerateToken() {
        // Arrange
        String email = "testEmail@gmail.com";
        String password = "testPassword";

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPasswordHash(password);

        // Mockowanie repozytorium
        doReturn(Optional.of(mockUser)).when(userRepository).findByUsername(email);

        // Mockowanie konfiguracji JWT
        doReturn(3600L).when(jwtConfig).getExpiration();  // Użycie doReturn zamiast when(...).thenReturn

        // Mockowanie generowania tokenu
        doReturn("token").when(jwtTokenProvider).generateToken(mockUser);

        // Act
        String token = authenticationService.authenticate(loginRequestDTO);

        // Assert
        assertNotNull(token);  // Sprawdzamy, czy token nie jest null
        assertEquals("token", token);  // Sprawdzamy, czy token ma wartość "token"
    }
}