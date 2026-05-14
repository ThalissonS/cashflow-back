package com.thalisson.cash_flow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Avisa o Spring que isso é um arquivo de configuração global
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todas as rotas (despesas, categorias, etc)
                .allowedOrigins("*") // ATENÇÃO: Libera qualquer front-end (depois trocamos pro IP do seu Angular)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"); // Libera todos os verbos HTTP
    }
}