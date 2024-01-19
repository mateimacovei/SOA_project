package com.matei;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final String SECRET = "982byv5p4yv8es7py5p4w9vny5p4wvtVzDh";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    private static SecretKey getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateToken(String username) {
        var now = System.currentTimeMillis();
        var token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
        return PREFIX + token;
    }

    public static String extractUsername(String token) {
        token = token.replace(PREFIX, "");
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
