package com.example.RedisProject;
import com.example.RedisProject.controller.RabbitQ;
import com.example.RedisProject.model.UserRequest;
import com.example.RedisProject.rabbitconfig.Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.example.RedisProject.rabbitconfig.Config.QUEUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRabbitQ {
    @Autowired
    private MockMvc mvc;
//    @MockBean
//    private RabbitTemplate rabbitTemplate;

    private RabbitAdmin rabbitAdmin;
   // @MockBean(name = "rabbitq")
    private RabbitQ rabbitQ;
    private UserRequest userRequest;
    private UserRequest userRequest1;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Captor
    private ArgumentCaptor<Message> amqpMessage;

    @Mock
    private RabbitTemplate rabbitTemplate;
    private CachingConnectionFactory connectionFactory;

    private RabbitMessagingTemplate messagingTemplate;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        messagingTemplate = new RabbitMessagingTemplate(rabbitTemplate);
    }
    @BeforeEach
    public void beforeEach() {
        this.connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

         rabbitTemplate =  new RabbitTemplate(connectionFactory);
        rabbitQ=new RabbitQ(rabbitTemplate);
        userRequest = new UserRequest(1L, "user1", 26, "01/01/1995", "chennai");
        userRequest1 = new UserRequest(2L, "user2", 26, "01/01/1995", "chennai");

    }

    @AfterEach
    public void after(){
        connectionFactory.destroy();
        rabbitTemplate.destroy();
        rabbitQ=null;
    }

    @Test
    @Order(1)
    public void testEnque() {
       rabbitQ.enque(userRequest);
        assertEquals("inserted",rabbitQ.enque(userRequest));

    }
    @Test
    @Order(2)
    public void testdeque() {
        rabbitQ.enque(userRequest);
        assertNull(rabbitQ.deque());

    }
    @Test
    @Order(3)
    public void testsize() {
        rabbitAdmin=new RabbitAdmin(rabbitTemplate);
        rabbitQ.enque(userRequest);
        assertEquals(0,rabbitQ.size());

    }
    @TestConfiguration
    static class MyTestConfig {


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

}
