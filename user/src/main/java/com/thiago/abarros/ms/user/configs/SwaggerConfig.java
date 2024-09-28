package com.thiago.abarros.ms.user.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI userMsOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("User MS API")
            .description("User MS API")
            .version("v0.0.1")
            .license(new License().name("MIT License")
                .url("https://opensource.org/licenses/MIT")))

        .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
        .components(new Components()
            .addSecuritySchemes("Bearer Auth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}
