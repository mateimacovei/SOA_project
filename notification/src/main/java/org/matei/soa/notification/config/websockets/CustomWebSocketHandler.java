package org.matei.soa.notification.config.websockets;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.matei.soa.notification.config.UserDetailsProvider;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler extends AbstractWebSocketHandler {

    private final Map<String, WebSocketSession> sessions;
    private final UserDetailsProvider userDetailsProvider;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        try {
            var token = Objects.requireNonNull(session.getUri()).getQuery().replace("token=", "");
            var user = userDetailsProvider.getUserDetails(token);
            String username = user.getUsername();
            sessions.put(username, session);
            log.info("connected user \"{}\"", username);
        } catch (Exception ex) {
            log.error("Error during auth", ex);
            session.close(SERVER_ERROR.withReason("User must be authenticated"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.entrySet().removeIf(x -> x.getValue().getId().equals(session.getId()));
    }
}
