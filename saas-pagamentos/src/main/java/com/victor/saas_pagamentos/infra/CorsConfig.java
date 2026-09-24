package com.victor.saas_pagamentos.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //Libera as rotas da API
                .allowedOrigins("http://localhost:3000", "http://localhost:5173") //Portas padrão do React e Vite/Vue
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //Verbos permitidos
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}