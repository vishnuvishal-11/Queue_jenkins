package com.example.RedisProject.controller;

import com.example.RedisProject.Repository.UserRequestRepository;
import lombok.extern.slf4j.Slf4j;
import com.example.RedisProject.model.UserRequest;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.example.RedisProject.rabbitconfig.Config;
import java.util.Properties;
import static org.springframework.amqp.rabbit.core.RabbitAdmin.QUEUE_MESSAGE_COUNT;
import static com.example.RedisProject.rabbitconfig.Config.*;

@Slf4j
@Component("rabbitq")
public class RabbitQ implements  QueueSelector {
   // @Autowired
    private final RabbitTemplate rabbitTemplate;

    private AmqpAdmin rabbitAdmin;

    public RabbitQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate=rabbitTemplate;
    }

//    @Autowired
//    @Qualifier("que")
//    private Queue queue;
//
//    @Autowired
//    private TopicExchange exchange;
//
//    @Autowired
//    private Binding binding;

    @Override
    public String enque(UserRequest userRequest) {
        rabbitTemplate.convertAndSend(Config.EXCHANGE, Config.ROUTING_KEY, userRequest);
        return "inserted";
    }

    @Override
    public Object deque() {

        try { Object message = rabbitTemplate.receiveAndConvert(QUEUE);
            return message;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public int size() {
       rabbitAdmin=new RabbitAdmin(rabbitTemplate);
        Properties property=rabbitAdmin.getQueueProperties(QUEUE);
        log.info("RabbitQ - size accessed...: ");
        if(!property.isEmpty())
        return (int) property.get(QUEUE_MESSAGE_COUNT);
        else return 0;

        }
    }


