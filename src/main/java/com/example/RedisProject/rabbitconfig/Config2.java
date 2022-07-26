package com.example.RedisProject.rabbitconfig;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config2 {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean("que")
    public Queue queue() {
        return new Queue(Config.QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(Config.EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Config.ROUTING_KEY);
    }
    @Bean
    public ConnectionFactory connectionFactory() {
        return new ConnectionFactory();
    }

}
