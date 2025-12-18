package com.library.notification_service.RabbitMQ_config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "loan.exchange";
    public static final String QUEUE = "loan.queue";
    public static final String ROUTING_KEY = "loan.created";

    @Bean
    public Queue loanQueue() {
        return new Queue(QUEUE, false);
    }

    @Bean
    public TopicExchange loanExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue loanQueue, TopicExchange loanExchange) {
        return BindingBuilder.bind(loanQueue).to(loanExchange).with(ROUTING_KEY);
    }
}