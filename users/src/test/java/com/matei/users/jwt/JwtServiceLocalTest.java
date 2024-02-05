package com.matei.users.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceLocalTest {
    @Test
    void encodeAndDecode() {
        var jwtService = new JwtServiceLocal();
        var userName = "user1";
        var token = jwtService.generateToken(userName);
        assertEquals(userName, jwtService.extractUsername(token));
    }

}