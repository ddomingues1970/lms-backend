package com.danieldomingues.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1) Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2) Usuários de teste (Postman/Angular)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails student = User.withUsername("student")
                .password(passwordEncoder.encode("student123"))
                .roles("STUDENT")
                .build();

        return new InMemoryUserDetailsManager(admin, student);
    }

    // 3) CORS para o Angular
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Ajuste se você usar outra porta/origem no front
        cfg.setAllowedOrigins(List.of("http://localhost:4200"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        cfg.setAllowCredentials(true);
        // Se precisar expor headers customizados no browser:
        // cfg.setExposedHeaders(List.of("Location"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // 4) Regras de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS + CSRF
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // Sessão stateless para API REST (bom com Basic/JWT)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Autorização por rotas
                .authorizeHttpRequests(auth -> auth
                        // Pré-flight do CORS
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints públicos (ex.: registro/login)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Administração
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Alunos e afins
                        .requestMatchers("/api/students/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/api/courses/**").hasAnyRole("ADMIN", "STUDENT")

                        // Qualquer outra rota precisa estar autenticada
                        .anyRequest().authenticated()
                )

                // Para testar facilmente no Postman (Basic Auth)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
