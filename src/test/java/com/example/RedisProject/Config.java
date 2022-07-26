package com.example.RedisProject;

import com.example.RedisProject.Redis.FactoryInterface;
import com.example.RedisProject.Redis.FilterSelector;
import com.example.RedisProject.Service.QueueInterface;
import com.example.RedisProject.controller.QueueClass;
import com.example.RedisProject.controller.QueueSelector;
import com.example.RedisProject.model.Converter;
import com.example.RedisProject.model.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class Config {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean(name = "customqueue")
    private QueueSelector customQueue;
    @MockBean(name = "rabbitq")
    private QueueSelector rabbitQ;
    @MockBean
    Converter converter;
    @MockBean
    QueueClass queueSelector;
    private UserRequest userRequest;
    ObjectMapper om = new ObjectMapper();
    @MockBean
    FilterSelector filterSelector;

    @MockBean(name = "custom")
    FactoryInterface factoryInterface;
    @MockBean(name = "cache")
    FactoryInterface factoryInterfaceRedis;
    @MockBean
    QueueInterface queueInterface;
    @MockBean
    RabbitTemplate redis1redisTemplate;
    @MockBean
    RabbitTemplate redis2redisTemplate;
    @MockBean
    RabbitAdmin rabbitAdmin;
}
