package com.matei.users.jwt;

import org.springframework.stereotype.Component;

public interface JwtService {
    String generateToken(String username);
    String extractUsername(String token);
}
