package org.matei.soa.notification;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class NotificationApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}