package com.matei.users.service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * use RabbitMQ to send a message to the main application service(s) that a user was deleted
 */
@Component
@RequiredArgsConstructor
public class PostsService {

    private static final String ROUTING_KEY = "delete.user";
    private static final String TOPIC_EXCHANGE_NAME = "soa.project";
    private final RabbitTemplate rabbitTemplate;

    public void handleDeletedUser(String username) {
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, ROUTING_KEY, username);
    }
}
