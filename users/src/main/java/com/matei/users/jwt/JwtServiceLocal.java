package com.matei.users.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Profile("jwtServiceLocal")
public class JwtServiceLocal implements JwtService{

    private static final String SECRET = "982byv5p4yv8es7py5p4w9vny5p4wvtVzDh";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    private static SecretKey getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public String generateToken(String username) {var now = System.currentTimeMillis();
        var token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
        return PREFIX + token;
    }

    @Override
    public String extractUsername(String token) {
        token = token.replace(PREFIX, "");
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
