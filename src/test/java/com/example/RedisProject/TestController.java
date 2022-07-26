package com.example.RedisProject;

import com.example.RedisProject.Redis.FactoryInterface;
import com.example.RedisProject.Redis.FilterSelector;
import com.example.RedisProject.Service.QueueInterface;
import com.example.RedisProject.ServiceImpl.Implementation;
import com.example.RedisProject.controller.CustomQueue;
import com.example.RedisProject.controller.QueueClass;
import com.example.RedisProject.controller.QueueSelector;
import com.example.RedisProject.model.Converter;
import com.example.RedisProject.model.UserRequest;
import com.example.RedisProject.model.UserRequestDto;
import com.example.RedisProject.rabbitconfig.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(QueueClass.class)
@ContextConfiguration(locations = "/test-context.xml")
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class TestController {
    @InjectMocks
    QueueClass queueClass;
    @Autowired
    private MockMvc mvc;
    @MockBean(name = "custom")
    FactoryInterface factoryInterface;
    @MockBean(name = "cache")
    FactoryInterface factoryInterface1;
    @MockBean(name = "customqueue")
    private QueueSelector customQueue;
    @MockBean(name = "rabbitq")
    private QueueSelector rabbitQ;
    @MockBean
    private Converter converter;
    @MockBean
    QueueClass queueSelector;
    private UserRequest userRequest;
    private UserRequest userRequest2;
    ObjectMapper mapper = new ObjectMapper();
    @MockBean
    FilterSelector filterSelector;
    @MockBean
    QueueInterface queueInterface;
    @MockBean
    RedisTemplate<String, Integer> redisTemplate;
    private String queuestring = "custom";
    UserRequestDto userRequestDto;
    UserRequestDto userRequestDto2;
    @MockBean
    RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        userRequest = new UserRequest(1L, "user_1", 26, "11-11-1995", "chennai");
        userRequest2 = new UserRequest(1L, "user_1", 26, "11-11-1995", "chennai");
        userRequestDto = converter.entityToDto(userRequest);
        userRequestDto2 = converter.entityToDto(userRequest2);
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN", setupBefore = TestExecutionEvent.TEST_METHOD, value = "admin")
    @Order(3)
    public void testEnque() throws Exception {
        queueClass.setQueuestring("rabbit");
        when(rabbitQ.enque(userRequest)).thenReturn("inserted");
        rabbitTemplate.convertAndSend(Config.EXCHANGE, Config.ROUTING_KEY, userRequest);
        String jsonRequest = mapper.writeValueAsString(userRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/queue/enque").with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonRequest).accept(MediaType.ALL);
        ResultActions perform = mvc.perform(requestBuilder);
        MvcResult result = perform.andDo(print()).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        assertEquals(200, status);
        assertEquals("inserted",rabbitQ.enque(userRequest));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN", setupBefore = TestExecutionEvent.TEST_METHOD, value = "admin")
   @Order(4)
    public void testEnque2() throws Exception {
        when(customQueue.enque(userRequest)).thenReturn("inserted");
        String jsonRequest = mapper.writeValueAsString(userRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/queue/enque").with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonRequest).accept(MediaType.ALL);
        ResultActions perform = mvc.perform(requestBuilder);
        MvcResult result = perform.andDo(print()).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        assertEquals(200, status);
        assertEquals("inserted",customQueue.enque(userRequest));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN", setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "admin")
    @Order(1)
    public void testdeque() throws Exception {
        when(customQueue.deque()).thenReturn(userRequestDto);
        when(rabbitQ.deque()).thenReturn(userRequestDto);
        when(converter.entityToDto(userRequest)).thenReturn(userRequestDto);


        QueueInterface queueInterface2=new Implementation();
        CustomQueue customQueue2 = new CustomQueue(queueInterface2);
        customQueue2.enque(userRequest);
        customQueue2.enque(userRequest2);
        customQueue.enque(userRequest);
        String jsonRequest = mapper.writeValueAsString(userRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/queue/deque").with(csrf().asHeader())
                .accept(MediaType.ALL);
        ResultActions perform = mvc.perform(requestBuilder);
        MvcResult result = perform.andDo(print()).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        assertEquals(200, status);
        assertEquals(userRequest, customQueue2.deque());
        assertEquals(userRequest2, customQueue2.deque());
        assertEquals(converter.entityToDto(userRequest),customQueue.deque());
        assertEquals(converter.entityToDto(userRequest),rabbitQ.deque());
        queueInterface2=null;
        customQueue2=null;
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN", setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "admin")
   @Order(2)
    public void testdisplay() throws Exception {
        QueueInterface queueInterface3=new Implementation();
        CustomQueue customQueue3 = new CustomQueue(queueInterface3);
        customQueue3.enque(userRequest);
        customQueue3.enque(userRequest2);
        List<UserRequest> list= new ArrayList<>();
        list.add(userRequest);
        list.add(userRequest2);
        when(queueInterface.display()).thenReturn(list);
      //  when(converter.entityToDto(list)).thenReturn(list);
        String jsonRequest = mapper.writeValueAsString(userRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/queue/display")
                .with(csrf().asHeader()).accept(MediaType.ALL);
        ResultActions perform = mvc.perform(requestBuilder);
        MvcResult result = perform.andDo(print()).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        assertEquals(200, status);
        assertEquals(list, queueInterface3.display());
        assertEquals(list, queueInterface.display());
        assertEquals(2, customQueue3.size());

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN", setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "admin")
    @Order(6)
    public void testdisplay2() throws Exception {
        queueClass=new QueueClass(customQueue,rabbitQ,converter,queueInterface);
        queueClass.setQueuestring("rabbit");
        queueClass.enque(userRequestDto);
        queueClass.deque();
        queueClass.enque(userRequestDto);
        queueClass.size();
        log.info("1 " + queueClass.deque());
        queueClass.setQueuestring("custom");
        queueClass.enque(userRequestDto);
        queueClass.deque();
        queueClass.deque();
        queueClass.size();
        userRequestDto = converter.entityToDto(userRequest);
        userRequestDto2 = converter.entityToDto(userRequest2);
        List<UserRequestDto> list= new ArrayList<>();
        list.add(userRequestDto);
        list.add(userRequestDto2);
        when(queueClass.display()).thenReturn(list);
        assertEquals(list, queueClass.display());


    }
    @TestConfiguration
    static class MyTestConfig {
        @Bean
        public JedisConnectionFactory jedisConnectionFactory() {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("redis", 6379);
            JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().build();
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration, jedisClientConfiguration);
            return jedisConnectionFactory;
        }

        @Bean
        public RedisTemplate<String, Integer> redisTemplate() {
            RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
            redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setConnectionFactory(jedisConnectionFactory());
            return redisTemplate;
        }

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
