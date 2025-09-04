package ru.m4oma.config;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Slf4j
@Configuration
public class RabbitListenerConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setErrorHandler(rabbitErrorHandler());

        return factory;
    }


    @Bean
    public ErrorHandler rabbitErrorHandler() {
        return new ConditionalRejectingErrorHandler(t -> {
            log.error("Rabbit listener error: {}", t.getMessage(), t);

            if (t instanceof ListenerExecutionFailedException ex &&
                    ex.getCause() instanceof EntityNotFoundException) {
                throw new AmqpRejectAndDontRequeueException("Mistress not found");
            }

            throw new AmqpRejectAndDontRequeueException("Unhandled error", t);
        });
    }

}