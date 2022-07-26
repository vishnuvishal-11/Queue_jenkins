package com.example.RedisProject;

import com.example.RedisProject.CustomEndpoint.HealthEndpoint;
import com.example.RedisProject.CustomEndpoint.InfoEndpoint;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TestCustomEndpoints {
  //  @MockBean(name = "rand")
    private HealthEndpoint healthEndpoint=new HealthEndpoint();
   // @MockBean(name = "in")
    private InfoEndpoint infoEndpoint=new InfoEndpoint();

    @Test
    public void testHealth() {
        healthEndpoint.health();
      //  when(healthEndpoint.health()).thenReturn();
    }

    @Test
    public void testInfo() {
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("name", "Spring Sample Application");
        userDetails.put("description", "This is my first spring boot application");
        userDetails.put("version", "2.0.0");
        Info.Builder builder =new Info.Builder();
        builder.withDetail("app", userDetails);
        builder.build();
        infoEndpoint.contribute(builder.withDetail("app", userDetails));
    }
}
