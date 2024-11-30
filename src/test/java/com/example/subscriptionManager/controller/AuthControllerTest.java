package com.example.subscriptionManager.controller;

import com.example.subscriptionManager.dto.LoginRequestDTO;
import com.example.subscriptionManager.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private final AuthenticationService authenticationService = mock(AuthenticationService.class);
    private final AuthController authController = new AuthController(authenticationService);

    @Test
    void login_ValidCredentials_ReturnsToken() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "password123");
        String expectedToken = "mocked-jwt-token";

        when(authenticationService.authenticate(loginRequest)).thenReturn(ResponseEntity.ok(expectedToken));

        ResponseEntity<String> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "wrong-password");

        when(authenticationService.authenticate(loginRequest)).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));

        ResponseEntity<String> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
