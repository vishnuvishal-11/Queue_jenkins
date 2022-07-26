package com.example.RedisProject.CustomEndpoint;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

@Component(value="rand")
public class HealthEndpoint implements HealthIndicator {
    @Override
    public Health health() {
        Calendar now = Calendar.getInstance();
        int minute = now.get(Calendar.MINUTE);

        Health.Builder status;
        if (minute % 2 == 0)
        status = Health.up();
        else
            status = Health.down();


        return status.build();
    }




}
