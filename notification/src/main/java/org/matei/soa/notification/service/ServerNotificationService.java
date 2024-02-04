package org.matei.soa.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ServerNotificationService {
    private final Map<String, WebSocketSession> sessions;

    public void sendNotification(String user, String message) {
        var session = sessions.get(user);
        if (session != null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
