package com.utkarsh.gupta.netflix_clone.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Netflix Clone API")
                        .version("1.0")
                        .description("API documentation for the Netflix Clone project"));
    }
}