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
                        // Preflight CORS
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Públicos (ex.: auth)
                        .requestMatchers("/api/auth/**").permitAll()

                        // >>> Cadastro de estudante (público)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/students").permitAll()

                        // Students (demais operações protegidas)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/students/**")
                        .hasAnyRole("ADMIN", "STUDENT") // ajuste aqui se quiser restringir mais
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/students/**")
                        .hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/students/**")
                        .hasRole("ADMIN")

                        // Courses — GET liberado para ADMIN/STUDENT; escrita só ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/courses/**")
                        .hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/courses/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/courses/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/courses/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/courses/**")
                        .hasRole("ADMIN")

                        // ENROLLMENTS
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/enrollments")
                        .hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/enrollments/**")
                        .hasAnyRole("ADMIN", "STUDENT")


                        // Administração
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Qualquer outra rota autenticada
                        .anyRequest().authenticated()
                )

                // Para testar facilmente no Postman (Basic Auth)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
