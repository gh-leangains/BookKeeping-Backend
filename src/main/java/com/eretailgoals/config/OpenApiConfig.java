package com.eretailgoals.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for API documentation
 * Configures Swagger UI and API documentation settings
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Bean
    public OpenAPI bookKeepingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookKeeping Backend API")
                        .description("Modern Spring Boot backend for BookKeeping application with Java 21")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("BookKeeping Team")
                                .email("support@bookkeeping.com")
                                .url("https://github.com/gh-leangains/BookKeeping-Backend"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080" + contextPath)
                                .description("Development server"),
                        new Server()
                                .url("https://api.bookkeeping.com" + contextPath)
                                .description("Production server")
                ));
    }
}