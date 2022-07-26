package com.example.RedisProject.rabbitconfig;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import  org.springframework.amqp.rabbit.connection.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class Config {
    public static final String QUEUE = "rabbit_queue";
    public static final String EXCHANGE = "rabbit_exchange";
    public static final String ROUTING_KEY = "rabbit_routingKey";
    @Autowired
    ConnectionFactory factory;
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }


}
