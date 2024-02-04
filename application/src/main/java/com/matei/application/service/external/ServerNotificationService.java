package com.matei.application.service.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * use apache kafka to send notifications
 */
@Slf4j
@Component
@Configuration
@RequiredArgsConstructor
public class ServerNotificationService {

    public static final String KAFKA_TOPIC_NAME = "soa";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public NewTopic topic1() {
        return new NewTopic(KAFKA_TOPIC_NAME, 1, (short) 1);
    }

    public void sendNotification(String user, String message) {
        kafkaTemplate.send(KAFKA_TOPIC_NAME, user + '/' + message).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("kafka event send. Offset: {}", result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send kafka event", ex);
            }
        });
    }
}
