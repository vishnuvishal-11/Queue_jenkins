package com.example.RedisProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import javax.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@SpringBootApplication(scanBasePackages ={"com.example.RedisProject.*","com.example.RedisProject.Service","com.example.RedisProject.model"})//, "com.example.controller", "com.example.Service", "com.example.ServiceImpl", "com.example.rabbitconfig", "com.example.Redis", "com.example.model", "com.example.ipServices", "com.example.security", "com.example.CustomEndpoint","com.example.Repository"})
@EnableRedisRepositories
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages="com.example.RedisProject.Repository")
@EntityScan(basePackages="com.example.RedisProject.model")
public class RedisProjectApplication {
    @Autowired
    private ObjectMapper objectMapper;
    public static void main(String[] args) {
       SpringApplication.run(RedisProjectApplication.class, args);

    }

@PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

}
