package org.matei.soa.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.matei.soa.notification.service.TextBoardException.internalServerError;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationsClient {
    public static final String KAFKA_TOPIC_NAME = "soa";

    private final ServerNotificationService serverNotificationService;

    @KafkaListener(topics = KAFKA_TOPIC_NAME)
    public void processMessage(String userAndMessage) {
        var split = userAndMessage.split("/", 2);
        if (split.length < 2) {
            throw internalServerError("Could not split user and message");
        }
        var user = split[0];
        var message = split[1];

        if (user.isEmpty() || message.isEmpty()) {
            throw internalServerError("Notification can not have empty components");
        }

        log.info("Received message {} for user {}", message, user);
        serverNotificationService.sendNotification(user, message);
    }
}
