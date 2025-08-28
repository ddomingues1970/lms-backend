package com.danieldomingues.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/students/**", "/h2-console/**").permitAll()
                        .requestMatchers("/api/courses/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic();

        // Libera o uso do H2 Console no navegador
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin123") // {noop} = sem criptografia
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
