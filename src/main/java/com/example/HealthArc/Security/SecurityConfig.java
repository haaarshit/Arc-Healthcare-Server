package com.example.HealthArc.Security;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((authz)-> authz
//                        .requestMatchers("api/v1/doctor/**").authenticated()
                        .anyRequest()
                        .permitAll()
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}