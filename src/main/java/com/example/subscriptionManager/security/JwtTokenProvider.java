package com.example.subscriptionManager.security;

import com.example.subscriptionManager.config.JwtConfig;
import com.example.subscriptionManager.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String getSecret() {
        return jwtConfig.getSecret();
    }

    public long getExpiration() {
        return jwtConfig.getExpiration();
    }


    public SecretKey generateSecretKey() {
        // Generujemy odpowiedni klucz o długości 512 bitów (64 bajty)
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(User user) {
        // Używamy wygenerowanego klucza do podpisania tokenu
        SecretKey key = generateSecretKey();

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + getExpiration()))
                .signWith(key)  // Podpisujemy token wygenerowanym kluczem
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        // Używamy wygenerowanego klucza do parsowania tokenu
        SecretKey key = generateSecretKey();

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = generateSecretKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
