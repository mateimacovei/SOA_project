package com.matei.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.matei.users.service.TextBoardException.forbidden;

@Component
@RequiredArgsConstructor
public class PasswordEncodingService {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(String clearText) {
        return passwordEncoder.encode(clearText);
    }

    public void throwIfNotMatching(String encoded, String clearText) {
        if (!passwordEncoder.matches(clearText, encoded)) {
            throw forbidden("Invalid password");
        }
    }
}
