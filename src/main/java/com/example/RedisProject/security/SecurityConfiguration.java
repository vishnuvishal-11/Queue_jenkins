package com.example.RedisProject.security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
@Slf4j
@Configuration
public class SecurityConfiguration  {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        UserDetails user2= User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user,user2);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().authorizeHttpRequests((authz) -> authz
                .antMatchers("/queue/size") .hasAnyRole("USER","ADMIN")
        ) .httpBasic(withDefaults());
        http.csrf().disable().cors().and()
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().hasRole("ADMIN")
                )
                .httpBasic(withDefaults());
        return http.build();
    }
    }

