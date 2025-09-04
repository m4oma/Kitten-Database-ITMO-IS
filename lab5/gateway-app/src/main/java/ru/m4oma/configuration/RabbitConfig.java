package ru.m4oma.configuration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange mistressExchange() {
        return new TopicExchange("mistress.exchange");
    }

    @Bean
    public TopicExchange kittenExchange() {
        return new TopicExchange("kitten.exchange");
    }
}
