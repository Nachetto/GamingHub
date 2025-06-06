package dev.nacho.ghub.common.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.Key;
import java.time.Duration;

@Configuration
public class JwtConfig {
    @Bean("JWT")
    public Key jwtKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}