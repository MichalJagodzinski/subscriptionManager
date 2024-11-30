package com.example.subscriptionManager.controller;

import com.example.subscriptionManager.dto.LoginRequestDTO;
import com.example.subscriptionManager.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private final AuthenticationService authenticationService = mock(AuthenticationService.class);
    private final AuthController authController = new AuthController(authenticationService);

    @Test
    void login_ValidCredentials_ReturnsToken() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "password123");
        String expectedToken = "mocked-jwt-token";

        when(authenticationService.authenticate(loginRequest)).thenReturn(expectedToken);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedToken, response.getBody());
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "wrong-password");
        when(authenticationService.authenticate(loginRequest)).thenThrow(new RuntimeException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
