package com.example.RedisProject;
import com.example.RedisProject.Service.QueueInterface;
import com.example.RedisProject.ServiceImpl.Implementation;
import com.example.RedisProject.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestQueueImplementation {
    private QueueInterface queueInterface2;
    private UserRequest userRequest;
    private UserRequest userRequest1;
    private UserRequest userRequest2;

    @BeforeEach
    public void beforeEach() {
        queueInterface2 = new Implementation();
        userRequest = new UserRequest(1L, "user1", 26, "01/01/1995", "chennai");
        userRequest1 = new UserRequest(2L, "user2", 26, "01/01/1995", "chennai");
        userRequest2 = new UserRequest(3L, "user3", 26, "01/01/1995", "chennai");
    }

    @AfterEach
    public void afterEach() {
        while (queueInterface2.size() != 0) {
            queueInterface2.deque();
        }
    }

    @Test
    @Order(1)
    public void testEnque() {
        queueInterface2.enque(userRequest);
        Assertions.assertEquals(queueInterface2.deque(), userRequest);
    }

    @Test
    @Order(2)
    public void testdeque() {
        Assertions.assertNull(queueInterface2.deque());
    }

    @Test
    @Order(3)
    public void testdequeAndEnque() {
        queueInterface2.enque(userRequest);
        queueInterface2.enque(userRequest1);
        queueInterface2.enque(userRequest2);
        Assertions.assertEquals(queueInterface2.deque(), userRequest);
        Assertions.assertEquals(queueInterface2.deque(), userRequest1);
        Assertions.assertEquals(queueInterface2.deque(), userRequest2);
    }

    @Test
    @Order(4)
    public void testPeek() {
        Assertions.assertNull(queueInterface2.peek());
        queueInterface2.enque(userRequest);
        queueInterface2.enque(userRequest1);
        queueInterface2.enque(userRequest2);
        Assertions.assertEquals(queueInterface2.peek(), userRequest2.toString());
    }

    @Test
    @Order(5)
    public void testSize() {
        Assertions.assertEquals(0, queueInterface2.size());
        queueInterface2.enque(userRequest);
        queueInterface2.enque(userRequest1);
        queueInterface2.enque(userRequest2);
        Assertions.assertEquals(3, queueInterface2.size());
    }

    @Test
    @Order(6)
    public void testDisplay() {
        UserRequest userRequest3 = new UserRequest(4L, "user4", 26, "01/01/1995", "chennai");
        queueInterface2.enque(userRequest);
        queueInterface2.enque(userRequest1);
        queueInterface2.enque(userRequest2);
        queueInterface2.enque(userRequest3);
        List<UserRequest> findAllActual = queueInterface2.display();
        List<UserRequest> findAllExpected = new ArrayList<>();
        findAllExpected.add(userRequest);
        findAllExpected.add(userRequest1);
        findAllExpected.add(userRequest2);
        findAllExpected.add(userRequest3);
        Assertions.assertEquals(findAllExpected, findAllActual);
    }

    @Test
    @Order(7)
    public void testDisplayWithNull() {
        Assertions.assertEquals(queueInterface2.display().toString(), "[]");
    }

}
