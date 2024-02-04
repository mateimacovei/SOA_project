package com.matei.application.service.external;

import com.matei.application.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * receive RabbitMQ messages from the users service
 */
@Slf4j
@Component
@Configuration
@RequiredArgsConstructor
public class UserApplicationMessageReceiver {
    private static final String RABBITMQ_TOPIC_EXCHANGE_NAME = "soa.project";
    private static final String RABBITMQ_QUEUE_NAME = "user-deletions";
    private final PostsService postsService;

    @Bean
    public Queue queue() {
        return new Queue(RABBITMQ_QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RABBITMQ_TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("delete.user");
    }

    @RabbitListener(queues = {RABBITMQ_QUEUE_NAME})
    public void receiveMessageFromFanout1(String message) {
        log.info("User deleted: {}", message);
        postsService.handleDeletedUser(message);
    }
}
