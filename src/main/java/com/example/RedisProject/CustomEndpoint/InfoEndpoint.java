package com.example.RedisProject.CustomEndpoint;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component(value = "in")
public class InfoEndpoint implements InfoContributor {
    Map<String, String> userDetails = new HashMap<>();

    @Override
    public void contribute(Info.Builder builder) {
        userDetails.put("name", "Spring Sample Application");
        userDetails.put("description", "This is my first spring boot application");
        userDetails.put("version", "2.0.0");
        builder.withDetail("app", userDetails);

    }
}
