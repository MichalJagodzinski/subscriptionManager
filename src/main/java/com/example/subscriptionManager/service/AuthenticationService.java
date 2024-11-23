package com.example.subscriptionManager.service;

import com.example.subscriptionManager.dto.LoginRequestDTO;
import com.example.subscriptionManager.entity.User;
import com.example.subscriptionManager.repository.UserRepository;
import com.example.subscriptionManager.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String authenticate(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("invalid email address or password"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash()))
            throw new RuntimeException("invalid email address or password");

        return jwtTokenProvider.generateToken(user);
    }
}
