package com.example.subscriptionManager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/users/register")) // Wyłączenie CSRF dla rejestracji
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register").permitAll() // Publiczny dostęp do rejestracji
                        .anyRequest().authenticated() // Wszystkie inne endpointy wymagają logowania
                )
                .httpBasic(basic -> basic.disable()) // Wyłączenie uwierzytelniania Basic
                .formLogin(login -> login.disable()); // Wyłączenie formularza logowania

        return http.build();
    }
}
