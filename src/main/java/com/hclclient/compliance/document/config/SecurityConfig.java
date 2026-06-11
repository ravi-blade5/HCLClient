package com.hclclient.compliance.document.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  @ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
  SecurityFilterChain securedApi(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health/**", "/actuator/info").permitAll().requestMatchers("/api/v1/regulatory-documents/**").authenticated().anyRequest().denyAll()).oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {})).build();
  }
  @Bean
  @ConditionalOnProperty(name = "app.security.enabled", havingValue = "false", matchIfMissing = true)
  SecurityFilterChain localDevelopmentApi(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
  }
}
