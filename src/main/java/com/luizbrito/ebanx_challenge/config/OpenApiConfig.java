package com.luizbrito.ebanx_challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration 
public class OpenApiConfig {
    
    @Bean 
    public OpenAPI ebanxChallengeOpenAPI() {
       // Configuration for OpenAPI can be added here if needed
       return new OpenAPI()
               .info(new Info().title("Ebanx Challenge API")
               .description("API documentation for Ebanx Challenge")
               .contact(new io.swagger.v3.oas.models.info.Contact()
                   .name("Luiz Brito da Rosa")
                   .email("luiz.rosa@ebanx.com"))
               .version("1.0.0"));
    }

}
