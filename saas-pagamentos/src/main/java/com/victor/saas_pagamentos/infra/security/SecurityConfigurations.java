package com.victor.saas_pagamentos.infra.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
    private String allowedOrigins;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/auth/register-admin").hasRole("ADMIN");

                    req.requestMatchers(HttpMethod.GET, "/actuator/health").permitAll();
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/planos").hasAnyRole("USER", "ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/planos").hasRole("ADMIN");

                    req.requestMatchers(HttpMethod.GET, "/clientes").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/clientes").hasRole("ADMIN");

                    req.requestMatchers(HttpMethod.GET, "/assinaturas").hasAnyRole("USER", "ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/assinaturas").hasAnyRole("USER", "ADMIN");
                    req.requestMatchers(HttpMethod.PATCH, "/assinaturas/*/cancelar").hasAnyRole("USER", "ADMIN");

                    req.requestMatchers(HttpMethod.GET, "/pagamentos").hasAnyRole("USER", "ADMIN");
                    req.requestMatchers(HttpMethod.PATCH, "/pagamentos/*/pagar").hasAnyRole("USER", "ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/pagamentos").hasRole("ADMIN"); // Só admin gera

                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(",")).map(String::trim).toList();
        config.setAllowedOrigins(origins);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}