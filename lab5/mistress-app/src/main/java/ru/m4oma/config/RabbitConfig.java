package ru.m4oma.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "mistress.exchange";

    public static final String QUEUE_GET_BY_ID = "mistress.get.by.id";
    public static final String QUEUE_GET_ALL = "mistress.get.all";
    public static final String QUEUE_CREATE = "mistress.create";
    public static final String QUEUE_DELETE = "mistress.delete";

    @Bean
    public TopicExchange mistressExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue getByIdQueue() {
        return new Queue(QUEUE_GET_BY_ID, false);
    }

    @Bean
    public Queue getAllQueue() {
        return new Queue(QUEUE_GET_ALL, false);
    }

    @Bean
    public Queue createQueue() {
        return new Queue(QUEUE_CREATE, false);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(QUEUE_DELETE, false);
    }

    @Bean
    public Binding bindingGetById(Queue getByIdQueue, TopicExchange mistressExchange) {
        return BindingBuilder.bind(getByIdQueue).to(mistressExchange).with(QUEUE_GET_BY_ID);
    }

    @Bean
    public Binding bindingGetAll(Queue getAllQueue, TopicExchange mistressExchange) {
        return BindingBuilder.bind(getAllQueue).to(mistressExchange).with(QUEUE_GET_ALL);
    }

    @Bean
    public Binding bindingCreate(Queue createQueue, TopicExchange mistressExchange) {
        return BindingBuilder.bind(createQueue).to(mistressExchange).with(QUEUE_CREATE);
    }

    @Bean
    public Binding bindingDelete(Queue deleteQueue, TopicExchange mistressExchange) {
        return BindingBuilder.bind(deleteQueue).to(mistressExchange).with(QUEUE_DELETE);
    }
}

