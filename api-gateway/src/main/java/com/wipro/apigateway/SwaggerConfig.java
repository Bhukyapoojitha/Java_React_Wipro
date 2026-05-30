package com.wipro.apigateway;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Account Service API",
        version = "1.0",
        description = "Account Microservice Endpoints"
    )
)
public class SwaggerConfig {
	
	@Bean
    public GroupedOpenApi accountApi() {
        return GroupedOpenApi.builder()
                .group("account-service")
                .pathsToMatch("/account/**")
                .build();
    }

    @Bean
    public GroupedOpenApi transactionApi() {
        return GroupedOpenApi.builder()
                .group("transaction-service")
                .pathsToMatch("/transaction/**")
                .build();
    }

}
