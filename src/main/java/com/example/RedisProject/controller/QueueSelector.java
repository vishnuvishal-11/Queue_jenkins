package com.example.RedisProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.RedisProject.model.UserRequest;

import java.io.IOException;

public interface QueueSelector {
    String enque(UserRequest userRequest) throws IOException;
    Object deque() throws JsonProcessingException;
    int size();
}
