package com.example.RedisProject.security;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class AccessList implements Serializable {

    private String ip;
    Counter counter;
    private Map<String, Counter> accessHistory = new HashMap<>();

    public void setAccessHistory(String ip, Counter counter) {
        accessHistory.put(ip, counter);
    }

    public AccessList(String ip, Counter counter) {
        this.ip = ip;
        this.counter = counter;
    }
}
