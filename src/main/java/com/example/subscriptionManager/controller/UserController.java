package com.example.subscriptionManager.controller;

import com.example.subscriptionManager.dto.UserRegistrationDTO;
import com.example.subscriptionManager.entity.User;
import com.example.subscriptionManager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User registedUser = userService.registerUser(registrationDTO);
        return new ResponseEntity<>(registedUser, HttpStatus.CREATED);
    }
}
