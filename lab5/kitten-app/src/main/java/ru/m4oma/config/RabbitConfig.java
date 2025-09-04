package ru.m4oma.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "kitten.exchange";

    public static final String GET_BY_MISTRESS_QUEUE = "kitten.get.by.mistress.id";
    public static final String GET_BY_MISTRESS_ROUTING_KEY = "kitten.get.by.mistress.id";

    public static final String CREATE_KITTEN_QUEUE = "kitten.create";
    public static final String CREATE_KITTEN_ROUTING_KEY = "kitten.create";

    public static final String REPAINT_KITTEN_QUEUE = "kitten.repaint";
    public static final String REPAINT_KITTEN_ROUTING_KEY = "kitten.repaint";

    public static final String GET_KITTEN_BY_ID_QUEUE = "kitten.get.by.id";
    public static final String GET_KITTEN_BY_ID_ROUTING_KEY = "kitten.get.by.id";

    @Bean
    public TopicExchange kittenExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue getByMistressQueue() {
        return new Queue(GET_BY_MISTRESS_QUEUE);
    }

    @Bean
    public Queue createKittenQueue() {
        return new Queue(CREATE_KITTEN_QUEUE);
    }

    @Bean
    public Queue repaintKittenQueue() {
        return new Queue(REPAINT_KITTEN_QUEUE);
    }

    @Bean
    public Queue getKittenByIdQueue() {
        return new Queue(GET_KITTEN_BY_ID_QUEUE);
    }

    @Bean
    public Binding bindGetByMistress() {
        return BindingBuilder.bind(getByMistressQueue()).to(kittenExchange()).with(GET_BY_MISTRESS_ROUTING_KEY);
    }

    @Bean
    public Binding bindCreateKitten() {
        return BindingBuilder.bind(createKittenQueue()).to(kittenExchange()).with(CREATE_KITTEN_ROUTING_KEY);
    }

    @Bean
    public Binding bindRepaintKitten() {
        return BindingBuilder.bind(repaintKittenQueue()).to(kittenExchange()).with(REPAINT_KITTEN_ROUTING_KEY);
    }

    @Bean
    public Binding bindGetKittenById() {
        return BindingBuilder.bind(getKittenByIdQueue()).to(kittenExchange()).with(GET_KITTEN_BY_ID_ROUTING_KEY);
    }
}

