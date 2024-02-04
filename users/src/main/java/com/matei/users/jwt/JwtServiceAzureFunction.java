package com.matei.users.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
@Profile("jwtServiceFaaS")
@RequiredArgsConstructor
public class JwtServiceAzureFunction implements JwtService {
    private final WebClient jwtFaasWebClient;

    @Override
    public String generateToken(String username) {
        return Objects.requireNonNull(jwtFaasWebClient.get()
                .uri("encodeUsername?username={username}", username)
                .retrieve()
                .bodyToMono(ValueHolder.class)
                .block()).value;
    }

    @Override
    public String extractUsername(String token) {
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
