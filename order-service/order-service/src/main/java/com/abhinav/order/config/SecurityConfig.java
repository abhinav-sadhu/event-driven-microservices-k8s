package com.abhinav.order.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Swagger UI को बिना टोकन के एक्सेस करने दें (पब्लिक रखें)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // बाकी सभी API कॉल्स (जैसे /order/publish) के लिए टोकन अनिवार्य (Mandatory) करें
                        .anyRequest().authenticated()
                )
                // JWT आधारित OAuth2 Resource Server इनेबल करें
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}