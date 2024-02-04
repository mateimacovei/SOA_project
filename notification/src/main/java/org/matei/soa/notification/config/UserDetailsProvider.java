package org.matei.soa.notification.config;

import lombok.RequiredArgsConstructor;
import org.matei.soa.notification.rest.model.user.UserAndRoleDto;
import org.matei.soa.notification.service.TextBoardException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class UserDetailsProvider {
    private final WebClient userServiceWebClient;

    /**
     * call the users microservice in order to get the details for a specific user
     */
    public UserAndRoleDto getUserDetails(String token) {
        return userServiceWebClient.get()
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(UserAndRoleDto.class)
                .onErrorMap(t -> TextBoardException.forbidden("Failed to authenticate using users microservice", t)).block();
    }
}
