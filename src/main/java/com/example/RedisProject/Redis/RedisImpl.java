package com.example.RedisProject.Redis;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component("cache")
@NoArgsConstructor
@Slf4j
public class RedisImpl implements FactoryInterface {
    @Value("${ratelimit.count}")
    int count ;

    @Value("${ratelimit.penalty.minutes}")
    int penalty;


    @Autowired
    @Qualifier("redis1")
    RedisTemplate<String, Integer> redis1redisTemplate;
    @Autowired
    @Qualifier("redis2")
    RedisTemplate<String, Integer> redis2redisTemplate;

    @Override
    public void filter(String ip)  {
        log.info("redisFilter is Used....");
        if (!(redis2redisTemplate.opsForHash().hasKey("DeniedList", ip)) && (!(redis1redisTemplate.opsForHash().hasKey("AllowedList", ip)))) {
            redis1redisTemplate.opsForHash().put("AllowedList", ip, 1);
            redis1redisTemplate.expire("AllowedList", penalty, TimeUnit.MINUTES);
            log.info("newly inserted");

        } else {
            if ((!(redis2redisTemplate.opsForHash().hasKey("DeniedList", ip))) && (redis1redisTemplate.opsForHash().hasKey("AllowedList", ip))) {
                int check = (int) redis1redisTemplate.opsForHash().get("AllowedList", ip);
                check = check + 1;
                if (check <= count) {
                    redis1redisTemplate.opsForHash().put("AllowedList", ip, check);

                }
                if (check == count+1) {
                    redis1redisTemplate.opsForHash().delete("AllowedList", ip);
                    redis2redisTemplate.opsForHash().put("DeniedList", ip, count+2);
                    redis2redisTemplate.expire("DeniedList", penalty, TimeUnit.MINUTES);
                    throw new RuntimeException("too much of  input in one minute...");
                }
            }
            if (redis2redisTemplate.opsForHash().hasKey("DeniedList", ip))
                throw new RuntimeException("too much of  input in one minute...");
        }

    }

}
