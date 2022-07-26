package com.example.RedisProject.Redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.RedisProject.security.AccessList;
import com.example.RedisProject.security.Counter;
import java.util.*;

@Slf4j
@Component("custom")
public class CustomImpl implements FactoryInterface {
    @Value("${ratelimit.count}")
    int count;
    @Value("${ratelimit.penalty.minutes}")
    int penalty;
   final  AccessList accessList;
    public CustomImpl( AccessList accessList) {
        this.accessList = accessList;
    }
    @Override
    public void filter(String ip) {
        log.info("CustomFilter is Used....");
        accessList.getAccessHistory().computeIfPresent(ip, (CounterNumber, targetDate) -> {
            Counter counter = accessList.getAccessHistory().get(ip);
            if ((Calendar.getInstance().getTime().compareTo(counter.getTargetDate()) >= 0 && counter.getNumber() != count)) {
                counter.setCounter(1, new Date(Calendar.getInstance().getTimeInMillis() + (penalty * 60 * 1000)));
                log.info("value" + counter.getNumber());
            } else if (counter.getNumber() <= count - 1)
                counter.setNumber(counter.getNumber() + 1);
            else if (counter.getNumber() == count) {
                counter.setCounter(count + 1, new Date(Calendar.getInstance().getTimeInMillis() + (penalty * 60 * 1000)));
                log.info("Too many inputs..retry after" + counter.getTargetDate());
                throw new RuntimeException("too much of  input in one minute...");
            } else if (counter.getNumber() > count)
                throw new RuntimeException("too much of  input in one minute...");
            return counter;
        });
        accessList.getAccessHistory().computeIfAbsent(ip, (counterObj) -> {
            Counter counter = new Counter();
            counter.setCounter(1, new Date(Calendar.getInstance().getTimeInMillis() + (penalty * 60 * 1000)));
            log.info("accssed value" + counter.getNumber());
            return counter;
        });
    }
}







