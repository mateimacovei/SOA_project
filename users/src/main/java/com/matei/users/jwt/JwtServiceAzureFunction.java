package com.matei.users.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
@Slf4j
@Component
@Profile("jwtServiceFaaS")
@RequiredArgsConstructor
public class JwtServiceAzureFunction implements JwtService {
    private final WebClient jwtFaasWebClient;

    @Override
    public String generateToken(String username) {
        log.info("Using azure faas auth");
        return Objects.requireNonNull(jwtFaasWebClient.get()
                .uri("encodeUsername?username={username}", username)
                .retrieve()
                .bodyToMono(ValueHolder.class)
                .block()).value;
    }

    @Override
    public String extractUsername(String token) {
        log.info("Using azure faas auth");
        return Objects.requireNonNull(jwtFaasWebClient.get()
                .uri("extractUsername")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ValueHolder.class)
                .block()).value;
    }

    private record ValueHolder(String value) {
    }
}
