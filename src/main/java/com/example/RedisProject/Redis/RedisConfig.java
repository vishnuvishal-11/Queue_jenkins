package com.example.RedisProject.Redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.*;

@Configuration
@EnableRedisRepositories(basePackages = {"Redis.AccessListRepository"," security.AccessList","Redis.FactoryInterface","Redis.FilterSelector","Redis.CustomImpl","Redis.RedisImpl"})
@PropertySource("classpath:application.properties")
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.redis.port}")
    private int REDIS_PORT;

    @Bean
  public  JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration(REDIS_HOST,REDIS_PORT);
        JedisClientConfiguration jedisClientConfiguration= JedisClientConfiguration.builder().build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration,jedisClientConfiguration);
        return jedisConnectionFactory;
    }

    @Bean("redis1")
    public RedisTemplate<String, Integer> redis1redisTemplate() {
        RedisTemplate<String, Integer> redis1redisTemplate = new RedisTemplate<>();
        redis1redisTemplate.setKeySerializer(new StringRedisSerializer());
        redis1redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
        redis1redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redis1redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redis1redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redis1redisTemplate;
    }
    @Bean("redis2")
    public RedisTemplate<String, Integer> redis2redisTemplate() {
        RedisTemplate<String, Integer> redis2redisTemplate = new RedisTemplate<>();
        redis2redisTemplate.setKeySerializer(new StringRedisSerializer());
        redis2redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
        redis2redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redis2redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redis2redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redis2redisTemplate;
    }

}